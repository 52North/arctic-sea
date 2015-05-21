/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.binding.pox;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlbeans.XmlObject;
import org.n52.iceland.binding.BindingConstants;
import org.n52.iceland.binding.SimpleBinding;
import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.decode.Decoder;
import org.n52.iceland.decode.DecoderKey;
import org.n52.iceland.decode.XmlNamespaceDecoderKey;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.ogc.ows.OwsExceptionReport;
import org.n52.iceland.ogc.sos.ConformanceClasses;
import org.n52.iceland.ogc.sos.Sos2Constants;
import org.n52.iceland.ogc.sos.SosConstants;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.iceland.util.XmlHelper;
import org.n52.iceland.util.http.MediaType;
import org.n52.iceland.util.http.MediaTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

/**
 * @since 4.0.0
 *
 */
public class PoxBinding extends SimpleBinding {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PoxBinding.class);
    private static final Set<String> CONFORMANCE_CLASSES = Collections
            .singleton(ConformanceClasses.SOS_V2_POX_BINDING);

    @Override
    public void doPostOperation(HttpServletRequest req,
                                HttpServletResponse res)
            throws HTTPException, IOException {
        AbstractServiceRequest<?> sosRequest = null;
        try {
            sosRequest = parseRequest(req);
            AbstractServiceResponse sosResponse = getServiceOperator(sosRequest)
                    .receiveRequest(sosRequest);
            writeResponse(req, res, sosResponse);
        } catch (OwsExceptionReport oer) {
            oer.setVersion(sosRequest != null ? sosRequest.getVersion() : null);
            writeOwsExceptionReport(req, res, oer);
        }
    }

    protected AbstractServiceRequest<?> parseRequest(HttpServletRequest request)
            throws OwsExceptionReport {
        XmlObject doc = XmlHelper.parseXmlRequest(request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("XML-REQUEST: {}", doc.xmlText());
        }
        Decoder<AbstractServiceRequest<?>, XmlObject> decoder =
                getDecoder(getDecoderKey(doc));
        return decoder.decode(doc).setRequestContext(getRequestContext(request));
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if(SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    public String getUrlPattern() {
        return BindingConstants.POX_BINDING_ENDPOINT;
    }

    @Override
    public boolean checkOperationHttpPostSupported(OperationKey k) {
        return hasDecoder(k, MediaTypes.TEXT_XML) ||
               hasDecoder(k, MediaTypes.APPLICATION_XML);
    }

    @Override
    public Set<MediaType> getSupportedEncodings() {
        return Sets.newHashSet(MediaTypes.TEXT_XML, MediaTypes.APPLICATION_XML);
    }

    @Override
    protected MediaType getDefaultContentType() {
        return MediaTypes.APPLICATION_XML;
    }
    
    private DecoderKey getDecoderKey(XmlObject doc) {
        return new XmlNamespaceDecoderKey(XmlHelper.getNamespace(doc), doc.getClass());
    }
}
