/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.coding.encode.OwsEncodingException;
import org.n52.iceland.event.events.ExceptionEvent;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.exception.ows.concrete.InvalidAcceptVersionsParameterException;
import org.n52.iceland.exception.ows.concrete.InvalidServiceOrVersionException;
import org.n52.iceland.exception.ows.concrete.InvalidServiceParameterException;
import org.n52.iceland.exception.ows.concrete.VersionNotSupportedException;
import org.n52.iceland.service.operator.ServiceOperator;
import org.n52.iceland.service.operator.ServiceOperatorRepository;
import org.n52.iceland.util.http.HttpUtils;
import org.n52.janmayen.Comparables;
import org.n52.janmayen.event.EventBus;
import org.n52.janmayen.http.HTTPHeaders;
import org.n52.janmayen.http.HTTPStatus;
import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.ows.exception.MissingServiceParameterException;
import org.n52.shetland.ogc.ows.exception.MissingVersionParameterException;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceKey;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceRequestContext;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.DecoderKey;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.decode.OperationDecoderKey;
import org.n52.svalbard.encode.Encoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncoderRepository;
import org.n52.svalbard.encode.ExceptionEncoderKey;
import org.n52.svalbard.encode.OperationResponseEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.NoEncoderForKeyException;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public abstract class SimpleBinding extends Binding {
    private static final String HTTP_MEDIA_TYPE_QUALITY_PARAM = "q";
    private static final Logger LOG = LoggerFactory.getLogger(SimpleBinding.class);

    private EventBus eventBus;
    private ServiceOperatorRepository serviceOperatorRepository;
    private EncoderRepository encoderRepository;
    private DecoderRepository decoderRepository;
    private HttpUtils httpUtils;

    public HttpUtils getHttpUtils() {
        return httpUtils;
    }

    @Inject
    public void setHttpUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    @Inject
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    @Inject
    public void setServiceOperatorRepository(ServiceOperatorRepository repo) {
        this.serviceOperatorRepository = repo;
    }

    public ServiceOperatorRepository getServiceOperatorRepository() {
        return this.serviceOperatorRepository;
    }

    @Inject
    public void setEncoderRepository(EncoderRepository encoderRepository) {
        this.encoderRepository = encoderRepository;
    }

    public EncoderRepository getEncoderRepository() {
        return encoderRepository;
    }

    @Inject
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    public DecoderRepository getDecoderRepository() {
        return decoderRepository;
    }

    @Override
    public Object handleEncodingException(HttpServletRequest request,
                                          HttpServletResponse response,
                                          EncodingException ex) throws HTTPException {
        try {
            OwsExceptionReport oer;
            if (ex instanceof OwsEncodingException) {
                oer = ((OwsEncodingException) ex).getCause();
            } else {
                oer = new NoApplicableCodeException().withMessage(ex.getMessage()).causedBy(ex);
            }
            eventBus.submit(new ExceptionEvent(oer));
            MediaType contentType = chooseResponseContentTypeForExceptionReport(HTTPHeaders.getAcceptHeader(request),
                                                                                getDefaultContentType());
            Object encoded = encodeOwsExceptionReport(oer, contentType);
            if (isUseHttpResponseCodes() && oer.hasStatus()) {
                response.setStatus(oer.getStatus().getCode());
            }
            return encoded;
        } catch (OwsExceptionReport e) {
            throw new HTTPException(HTTPStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    protected abstract boolean isUseHttpResponseCodes();

    protected OwsServiceRequestContext getRequestContext(HttpServletRequest req) {
        return OwsServiceRequestContext.fromRequest(req);
    }

    protected boolean isVersionSupported(String service, String acceptVersion) {
        return getServiceOperatorRepository().isVersionSupported(service, acceptVersion);
    }

    protected boolean isServiceSupported(String service) {
        return getServiceOperatorRepository().isServiceSupported(service);
    }

    protected <F, T> Decoder<F, T> getDecoder(DecoderKey key) {
        return this.decoderRepository.getDecoder(key);
    }

    protected <F, T> Encoder<F, T> getEncoder(EncoderKey key) {
        return this.encoderRepository.getEncoder(key);
    }

    protected boolean hasDecoder(DecoderKey key) {
        return this.decoderRepository.hasDecoder(key);
    }

    protected boolean hasEncoder(EncoderKey key) {
        return this.encoderRepository.hasEncoder(key);
    }

    protected boolean hasDecoder(OwsOperationKey key,
                                 MediaType mediaType) {
        return hasDecoder(new OperationDecoderKey(key, mediaType));
    }

    protected boolean hasEncoder(OwsOperationKey key,
                                 MediaType mediaType) {
        return hasEncoder(new OperationResponseEncoderKey(key, mediaType));
    }

    protected boolean hasEncoder(OwsServiceResponse response,
                                 MediaType mediaType) {
        return hasEncoder(new OwsOperationKey(response), mediaType);
    }

    protected MediaType chooseResponseContentType(OwsServiceResponse response,
                                                  List<MediaType> acceptHeader,
                                                  MediaType defaultContentType) throws HTTPException {
        /*
         * TODO get a list of response content types and check against
         * wildcards/qualities
         */
        if (!acceptHeader.isEmpty()) {
            if (!response.isSetContentType()) {
                for (MediaType mt : acceptHeader) {
                    MediaType mediaType = mt.withoutParameter(HTTP_MEDIA_TYPE_QUALITY_PARAM);
                    if (defaultContentType.isCompatible(mediaType)) {
                        return defaultContentType;
                    } else if (hasEncoder(response, mediaType)) {
                        return mediaType;
                    }
                }
                // no encoder for any accept header content type
                throw new HTTPException(HTTPStatus.NOT_ACCEPTABLE);
            } else {
                for (MediaType mt : acceptHeader) {
                    MediaType mediaType = mt.withoutParameter(HTTP_MEDIA_TYPE_QUALITY_PARAM);
                    if (response.getContentType().isCompatible(mediaType)) {
                        return response.getContentType();
                    }
                }
                // incompatible response content type and accept header
                throw new HTTPException(HTTPStatus.NOT_ACCEPTABLE);
            }
        } else {
            if (!response.isSetContentType()) {
                return defaultContentType;
            } else {
                MediaType mediaType = response.getContentType().withoutParameter(HTTP_MEDIA_TYPE_QUALITY_PARAM);
                if (hasEncoder(response, mediaType)) {
                    return mediaType;
                }
            }
            // no encoder for response content type
            throw new HTTPException(HTTPStatus.NOT_ACCEPTABLE);
        }
    }

    protected MediaType chooseResponseContentTypeForExceptionReport(List<MediaType> acceptHeader,
                                                                    MediaType defaultContentType) throws HTTPException {
        /*
         * TODO get a list of response content types and check against
         * wildcards/qualities
         */
        if (acceptHeader.isEmpty()) {
            return defaultContentType;
        }
        for (MediaType mt : acceptHeader) {
            MediaType mediaType = mt.withoutParameter(HTTP_MEDIA_TYPE_QUALITY_PARAM);
            if (defaultContentType.isCompatible(mediaType)) {
                return defaultContentType;
            } else if (hasEncoder(new ExceptionEncoderKey(mediaType))) {
                return mediaType;
            }
        }
        throw new HTTPException(HTTPStatus.NOT_ACCEPTABLE);
    }

    protected ServiceOperator getServiceOperator(OwsServiceKey sokt) {
        return getServiceOperatorRepository().getServiceOperator(sokt);
    }

    protected ServiceOperator getServiceOperator(OwsServiceRequest request) throws OwsExceptionReport {
        checkServiceOperatorKeyTypes(request);
        String service = request.getService();
        String version = request.getVersion();
        if (request instanceof GetCapabilitiesRequest) {
            GetCapabilitiesRequest gcr = (GetCapabilitiesRequest) request;
            if (gcr.isSetAcceptVersions()) {
                return gcr.getAcceptVersions().stream().map(v -> new OwsServiceKey(service, v))
                        .map(this::getServiceOperator).filter(Objects::nonNull).findFirst()
                        .orElseThrow(() -> new InvalidServiceOrVersionException(service, version));
            } else {
                Set<String> supportedVersions = serviceOperatorRepository.getSupportedVersions(service);
                String newest = supportedVersions.stream().max(Comparables.version())
                        .orElseThrow(() -> new InvalidServiceParameterException(service));
                return getServiceOperator(new OwsServiceKey(service, newest));
            }
        } else {
            return getServiceOperator(new OwsServiceKey(service, version));
        }
    }

    protected void checkServiceOperatorKeyTypes(OwsServiceRequest request) throws OwsExceptionReport {
        String service = request.getService();
        String version = request.getVersion();
        if (service == null || service.isEmpty()) {
            throw new MissingServiceParameterException();
        } else if (!getServiceOperatorRepository().isServiceSupported(service)) {
            throw new InvalidServiceParameterException(service);
        } else if (request instanceof GetCapabilitiesRequest) {
            GetCapabilitiesRequest gcr = (GetCapabilitiesRequest) request;
            if (gcr.isSetAcceptVersions() && !gcr.getAcceptVersions().stream()
                .anyMatch(v -> isVersionSupported(service, v))) {
                throw new InvalidAcceptVersionsParameterException(gcr.getAcceptVersions());
            }
        } else if (version == null || version.isEmpty()) {
            throw new MissingVersionParameterException();
        } else if (!isVersionSupported(service, version)) {
            throw new VersionNotSupportedException();
        }
    }

    protected void writeResponse(HttpServletRequest request,
                                 HttpServletResponse response,
                                 OwsServiceResponse serviceResponse) throws HTTPException, IOException {
        try {
            MediaType contentType = chooseResponseContentType(serviceResponse,
                                                              HTTPHeaders.getAcceptHeader(request),
                                                              getDefaultContentType());
            if (!serviceResponse.isSetContentType()) {
                serviceResponse.setContentType(contentType);
            }
            httpUtils.writeObject(request, response, contentType, serviceResponse, this);
        } finally {
            serviceResponse.close();
        }
    }

    protected Object encodeResponse(OwsServiceResponse response,
                                    MediaType contentType) throws OwsExceptionReport {

        try {
            OperationResponseEncoderKey key
                    = new OperationResponseEncoderKey(new OwsOperationKey(response), contentType);
            Encoder<Object, OwsServiceResponse> encoder = getEncoder(key);
            if (encoder == null) {
                throw new NoEncoderForKeyException(key);
            }
            return encoder.encode(response);
        } catch (EncodingException ex) {
            throw new NoApplicableCodeException().withMessage(ex.getMessage()).causedBy(ex);
        }
    }

    protected void writeOwsExceptionReport(HttpServletRequest request,
                                           HttpServletResponse response,
                                           OwsExceptionReport oer) throws HTTPException {
        try {
            this.eventBus.submit(new ExceptionEvent(oer));
            MediaType contentType
                    = chooseResponseContentTypeForExceptionReport(HTTPHeaders.getAcceptHeader(request),
                                                                  getDefaultContentType());
            Object encoded = encodeOwsExceptionReport(oer, contentType);
            if (isUseHttpResponseCodes() && oer.hasStatus()) {
                response.setStatus(oer.getStatus().getCode());
            }
            httpUtils.writeObject(request, response, contentType, encoded, this);
        } catch (IOException | OwsExceptionReport e) {
            throw new HTTPException(HTTPStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    protected abstract MediaType getDefaultContentType();

    protected Object encodeOwsExceptionReport(OwsExceptionReport oer,
                                              MediaType contentType) throws OwsExceptionReport, HTTPException {
        Encoder<Object, OwsExceptionReport> encoder = getEncoder(new ExceptionEncoderKey(contentType));
        if (encoder == null) {
            LOG.error("Can't find OwsExceptionReport encoder for Content-Type {}", contentType);
            throw new HTTPException(HTTPStatus.UNSUPPORTED_MEDIA_TYPE);
        }
        try {
            return encoder.encode(oer);
        } catch (EncodingException ex) {
            throw new NoApplicableCodeException().withMessage(ex.getMessage()).causedBy(ex);
        }
    }

}
