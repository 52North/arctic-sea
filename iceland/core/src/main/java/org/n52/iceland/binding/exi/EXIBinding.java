/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding.exi;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import com.siemens.ct.exi.core.EXIFactory;
import com.siemens.ct.exi.core.exceptions.EXIException;
import com.siemens.ct.exi.main.api.sax.EXISource;

import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.binding.MediaTypeBindingKey;
import org.n52.iceland.binding.SimpleBinding;
import org.n52.iceland.coding.decode.OwsDecodingException;
import org.n52.iceland.exception.HTTPException;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.exception.InvalidParameterValueException;
import org.n52.shetland.ogc.ows.exception.NoApplicableCodeException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.svalbard.decode.Decoder;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;


/**
 * Binding implementation for EXI - Efficient XML Interchange See See
 * <a href="http://www.w3.org/TR/exi/">http://www.w3.org/TR/exi/</a>
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 2.0.0
 *
 */
public class EXIBinding extends SimpleBinding {
    private static final Set<String> CONFORMANCE_CLASSES =
            Collections.singleton("http://www.opengis.net/spec/SOS/2.0/conf/exi");
    private static final Logger LOGGER = LoggerFactory.getLogger(EXIBinding.class);
    private static final Set<BindingKey> KEYS =
            Collections.singleton(new MediaTypeBindingKey(MediaTypes.APPLICATION_EXI));
    private final EXIUtils exiUtils;

    @Inject
    public EXIBinding(EXIUtils exiUtils) {
        this.exiUtils = exiUtils;
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if (SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    protected MediaType getDefaultContentType() {
        return MediaTypes.APPLICATION_EXI;
    }

    @Override
    protected boolean isUseHttpResponseCodes() {
        return true;
    }

    @Override
    public boolean checkOperationHttpPostSupported(OwsOperationKey k)
            throws HTTPException {
        return hasDecoder(k, MediaTypes.TEXT_XML) || hasDecoder(k, MediaTypes.APPLICATION_XML);
    }

    @Override
    public void doPostOperation(HttpServletRequest req, HttpServletResponse res)
            throws HTTPException, IOException {
        OwsServiceRequest sosRequest = null;
        try {
            sosRequest = parseRequest(req);
            OwsServiceResponse sosResponse = getServiceOperator(sosRequest).receiveRequest(sosRequest);
            writeResponse(req, res, sosResponse);
        } catch (OwsExceptionReport oer) {
            oer.setVersion(sosRequest != null ? sosRequest.getVersion() : null);
            writeOwsExceptionReport(req, res, oer);
        }
    }

    /**
     * Parse and decode the incoming EXI encoded {@link InputStream}
     *
     * @param request
     *            {@link HttpServletRequest} with EXI encoded
     *            {@link InputStream}
     * @return {@link OwsServiceRequest} from EXI encoded {@link InputStream}
     * @throws OwsExceptionReport
     *             If an error occurs during parsing
     */
    protected OwsServiceRequest parseRequest(HttpServletRequest request)
            throws OwsExceptionReport {
        XmlObject doc = decode(request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("EXI-REQUEST: {}", doc.xmlText());
        }
        Decoder<OwsServiceRequest, XmlObject> decoder = getDecoder(CodingHelper.getDecoderKey(doc));
        try {
            return decoder.decode(doc).setRequestContext(getRequestContext(request));
        } catch (OwsDecodingException ex) {
            throw ex.getCause();
        } catch (DecodingException ex) {
            throw new InvalidParameterValueException().withMessage(ex.getMessage()).causedBy(ex)
                    .at(ex.getLocation().orElse(null));
        }
    }

    /**
     * Parse the incoming EXI encoded {@link InputStream} transform to
     * {@link XmlObject}
     *
     * @param request
     *            {@link HttpServletRequest} with EXI encoded
     *            {@link InputStream}
     * @return {@link XmlObject} created from the EXI encoded
     *         {@link InputStream}
     * @throws OwsExceptionReport
     *             If an error occurs during parsing
     */
    protected XmlObject decode(HttpServletRequest request)
            throws OwsExceptionReport {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            EXIFactory ef = this.exiUtils.newEXIFactory();

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            if (ef.isFragment()) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            }
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            // decode EXI encoded InputStream
            EXISource exiSource = new EXISource(ef);
            XMLReader exiReader = exiSource.getXMLReader();
            exiReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            InputSource inputSource = new InputSource(request.getInputStream());
            inputSource.setEncoding(request.getCharacterEncoding());
            SAXSource saxSource = new SAXSource(inputSource);
            saxSource.setXMLReader(exiReader);
            transformer.transform(saxSource, new StreamResult(os));

            // create XmlObject from OutputStream
            return XmlHelper.parseXmlString(os.toString(StandardCharsets.UTF_8.name()));
        } catch (IOException | EXIException | SAXNotRecognizedException | SAXNotSupportedException ex) {
            throw new NoApplicableCodeException().causedBy(ex).withMessage("Error while reading request! Message: %s",
                    ex.getMessage());
        } catch (TransformerException ex) {
            throw new NoApplicableCodeException().causedBy(ex)
                    .withMessage("Error while transforming request! Message: %s", ex.getMessage());
        } catch (DecodingException ex) {
            throw new NoApplicableCodeException().causedBy(ex).withMessage("Error while parsing request! Message: %s",
                    ex.getMessage());
        }
    }

    @Override
    public Set<BindingKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }
}
