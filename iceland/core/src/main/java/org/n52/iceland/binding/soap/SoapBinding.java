/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.iceland.binding.soap;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.n52.iceland.binding.AbstractXmlBinding;
import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.binding.MediaTypeBindingKey;
import org.n52.iceland.coding.encode.OwsEncodingException;
import org.n52.iceland.event.events.ExceptionEvent;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.service.CommunicationObjectWithSoapHeader;
import org.n52.iceland.util.http.HttpUtils;
import org.n52.janmayen.http.HTTPHeaders;
import org.n52.janmayen.http.HTTPStatus;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceRequestContext;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.soap.SoapChain;
import org.n52.shetland.w3c.soap.SoapConstants;
import org.n52.shetland.w3c.soap.SoapHeader;
import org.n52.shetland.w3c.soap.SoapRequest;
import org.n52.shetland.w3c.soap.SoapResponse;
import org.n52.shetland.w3c.wsa.WsaMessageIDHeader;
import org.n52.shetland.w3c.wsa.WsaReplyToHeader;
import org.n52.shetland.w3c.wsa.WsaToHeader;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.XmlEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

/**
 * {@link Binding} implementation for SOAP encoded requests
 *
 * @since 1.0.0
 *
 */
public class SoapBinding extends AbstractXmlBinding<SoapRequest> {

    private static final Set<BindingKey> KEYS = Collections
            .singleton(new MediaTypeBindingKey(MediaTypes.APPLICATION_SOAP_XML));

    private HttpUtils httpUtils;

    @Override
    public Set<BindingKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }

    @Override
    public boolean checkOperationHttpPostSupported(OwsOperationKey k) {
        return hasDecoder(k, MediaTypes.TEXT_XML) ||
               hasDecoder(k, MediaTypes.APPLICATION_XML);
    }

    @Override
    protected boolean isUseHttpResponseCodes() {
        return false;
    }

    @Override
    protected MediaType getDefaultContentType() {
        return MediaTypes.APPLICATION_SOAP_XML;
    }

    @Override
    public void doPostOperation(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
            throws HTTPException, IOException {
        final SoapChain chain = new SoapChain(httpRequest, httpResponse);
        try {
            parseSoapRequest(chain);
            checkForContext(chain, getRequestContext(httpRequest));
            createSoapResponse(chain);
            if (!chain.getSoapResponse().isSetSoapFault()) {
                // parseBodyRequest(chain);
                createBodyResponse(chain);
                writeResponse(chain);
            } else {
                writeFault(chain);
            }
        } catch (OwsExceptionReport t) {
            writeOwsExceptionReport(chain, t);
        }
    }

    private void checkForContext(SoapChain chain, OwsServiceRequestContext requestContext) {
        if (chain != null) {
            if (chain.getBodyRequest() != null) {
                chain.getBodyRequest().setRequestContext(requestContext);
            }
            if (chain.getSoapRequest() != null && chain.getSoapRequest().getSoapBodyContent() != null) {
                chain.getSoapRequest().getSoapBodyContent().setRequestContext(requestContext);
            }
        }
    }

    private void parseSoapRequest(SoapChain soapChain) throws OwsExceptionReport {
        String soapAction = checkSoapHeader(soapChain.getHttpRequest());
        SoapRequest soapRequest = decode(soapChain.getHttpRequest());
        if (soapRequest.getSoapAction() == null && soapAction != null) {
            soapRequest.setAction(soapAction);
        }
        soapChain.setSoapRequest(soapRequest);
    }

    private void createSoapResponse(SoapChain chain) {
        SoapResponse soapResponse = new SoapResponse();
        soapResponse.setSoapVersion(chain.getSoapRequest().getSoapVersion());
        soapResponse.setSoapNamespace(chain.getSoapRequest().getSoapNamespace());
        soapResponse.setHeader(checkSoapHeaders(chain.getSoapRequest().getSoapHeader()));
        if (chain.getSoapRequest().hasSoapFault()) {
            soapResponse.setSoapFault(chain.getSoapRequest().getSoapFault());
        }
        chain.setSoapResponse(soapResponse);
    }

    private void createBodyResponse(SoapChain chain) throws OwsExceptionReport {
        OwsServiceRequest req = chain.getSoapRequest().getSoapBodyContent();
        chain.setBodyResponse(getServiceOperator(req).receiveRequest(req));
    }

    private Object encodeSoapResponse(SoapChain chain) throws OwsExceptionReport, NoEncoderForKeyException {
        EncoderKey key = new XmlEncoderKey(chain.getSoapResponse().getSoapNamespace(),
                                           chain.getSoapResponse().getClass());
        Encoder<?, SoapResponse> encoder = getEncoder(key);
        if (encoder != null) {
            try {
                return encoder.encode(chain.getSoapResponse());
            } catch (OwsEncodingException ex) {
                throw ex.getCause();
            } catch (EncodingException ex) {
                throw new NoApplicableCodeException().withMessage(ex.getMessage()).causedBy(ex);
            }
        } else {
            NoEncoderForKeyException cause = new NoEncoderForKeyException(key);
            throw new NoApplicableCodeException().withMessage(cause.getMessage()).causedBy(cause);
        }
    }

    private void writeOwsExceptionReport(SoapChain chain, OwsExceptionReport owse) throws HTTPException, IOException {
        String version = chain.hasBodyRequest() ? chain.getBodyRequest().getVersion() : null;
        getEventBus().submit(new ExceptionEvent(owse));
        chain.getSoapResponse().setException(owse.setVersion(version));
        writeFault(chain);
    }

    private void writeFault(SoapChain chain) throws HTTPException, IOException {
        if (!chain.getSoapResponse().hasSoapVersion()) {
            chain.getSoapResponse().setSoapVersion(SoapConstants.SOAP_1_2_VERSION);
        }
        if (!chain.getSoapResponse().hasSoapNamespace()) {
            chain.getSoapResponse().setSoapNamespace(SoapConstants.NS_SOAP_12);
        }
        if (chain.getSoapResponse().hasException() && chain.getSoapResponse().getException().hasStatus()) {
            chain.getHttpResponse().setStatus(chain.getSoapResponse().getException().getStatus().getCode());
        }
        checkSoapInjection(chain);
        try {
            httpUtils.writeObject(chain.getHttpRequest(), chain.getHttpResponse(), checkMediaType(chain),
                    encodeSoapResponse(chain), this);
        } catch (OwsExceptionReport | NoEncoderForKeyException t) {
            throw new HTTPException(HTTPStatus.INTERNAL_SERVER_ERROR, t);
        }
    }

    private void writeResponse(SoapChain chain) throws IOException, HTTPException {
        MediaType contentType = chooseResponseContentType(chain.getBodyResponse(),
                                                          HTTPHeaders.getAcceptHeader(chain.getHttpRequest()),
                                                          getDefaultContentType());
        // TODO allow other bindings to encode response as soap messages
        if (contentType.isCompatible(getDefaultContentType())) {
            checkSoapInjection(chain);
            httpUtils.writeObject(chain.getHttpRequest(), chain.getHttpResponse(), checkMediaType(chain), chain, this);
        } else {
            httpUtils.writeObject(chain.getHttpRequest(), chain.getHttpResponse(), contentType,
                                  chain.getBodyResponse(), this);
        }
    }

    /**
     * Check the {@link MediaType}
     *
     * @param chain SoapChain to check
     *
     * @return the valid {@link MediaType}
     */
    private MediaType checkMediaType(SoapChain chain) {
        if (chain.getBodyRequest() instanceof GetCapabilitiesRequest) {
            GetCapabilitiesRequest r = (GetCapabilitiesRequest) chain.getBodyRequest();
            if (r.isSetAcceptFormats()) {
                return MediaType.parse(r.getAcceptFormats().get(0));
            }
        }
        return MediaTypes.APPLICATION_SOAP_XML;
    }

    /**
     * Check if SoapHeader information is contained in the body response and add the header information to the
     * {@link SoapResponse}
     *
     * @param chain SoapChain to check
     */
    private void checkSoapInjection(SoapChain chain) {
        if (chain.getBodyResponse() instanceof CommunicationObjectWithSoapHeader) {
            CommunicationObjectWithSoapHeader soapHeaderObject = (CommunicationObjectWithSoapHeader) chain
                    .getBodyResponse();
            if (soapHeaderObject.isSetSoapHeader()) {
                List<SoapHeader> headers = ((CommunicationObjectWithSoapHeader) chain.getSoapRequest()).getSoapHeader();
                // TODO do things
                chain.getSoapResponse().setHeader(checkSoapHeaders(headers));
            }
        }
    }

    private List<SoapHeader> checkSoapHeaders(List<SoapHeader> headers) {
        if (CollectionHelper.isNotEmpty(headers)) {
            return headers.stream().map(header -> {
                if (header instanceof WsaMessageIDHeader) {
                    return ((WsaMessageIDHeader) header).getRelatesToHeader();
                } else if (header instanceof WsaReplyToHeader) {
                    return ((WsaReplyToHeader) header).getToHeader();
                } else if (!(header instanceof WsaToHeader)) {
                    return header;
                } else {
                    return null;
                }
            }).filter(Objects::nonNull).collect(toList());
        }
        return null;
    }

    protected String checkSoapHeader(HttpServletRequest request) {
        Enumeration<?> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerNameKey = (String) headerNames.nextElement();
            if (headerNameKey.equalsIgnoreCase("type")) {
                String type = request.getHeader(headerNameKey);
                String[] typeArray = type.split(";");
                for (String string : typeArray) {
                    if (string.startsWith("action")) {
                        String soapAction = string.replace("action=", "");
                        soapAction = soapAction.replace("\"", "");
                        soapAction = soapAction.trim();
                        return soapAction;
                    }
                }
            } else if (headerNameKey.equalsIgnoreCase("SOAPAction")) {
                return request.getHeader(headerNameKey);
            }
        }
        return null;
    }

    @Override
    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    @Inject
    @Override
    public void setHttpUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

}
