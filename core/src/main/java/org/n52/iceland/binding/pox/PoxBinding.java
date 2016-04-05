/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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

import org.n52.iceland.binding.AbstractXmlBinding;
import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingConstants;
import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.binding.MediaTypeBindingKey;
import org.n52.iceland.binding.PathBindingKey;
import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.ogc.sos.ConformanceClasses;
import org.n52.iceland.ogc.sos.Sos2Constants;
import org.n52.iceland.ogc.sos.SosConstants;
import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.iceland.service.MiscSettings;
import org.n52.iceland.util.http.MediaType;
import org.n52.iceland.util.http.MediaTypes;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link Binding} implementation for POX (XML) encoded requests
 * @since 1.0.0
 *
 */
@Configurable
public class PoxBinding extends AbstractXmlBinding {

    @Deprecated // SOS-specific
    private static final Set<String> CONFORMANCE_CLASSES = Collections
            .singleton(ConformanceClasses.SOS_V2_POX_BINDING);

     private static final ImmutableSet<BindingKey> KEYS = ImmutableSet.<BindingKey>builder()
            .add(new PathBindingKey(BindingConstants.POX_BINDING_ENDPOINT))
            .add(new MediaTypeBindingKey(MediaTypes.APPLICATION_XML))
            .add(new MediaTypeBindingKey(MediaTypes.TEXT_XML))
            .build();

    private static final Logger LOG = LoggerFactory.getLogger(PoxBinding.class);

    private boolean useHttpResponseCodes;

    @Setting(MiscSettings.HTTP_STATUS_CODE_USE_IN_KVP_POX_BINDING)
    public void setUseHttpResponseCodes(boolean useHttpResponseCodes) {
        this.useHttpResponseCodes = useHttpResponseCodes;
    }

    @Override
    protected boolean isUseHttpResponseCodes() {
        return this.useHttpResponseCodes;
    }

    @Override
    public Set<BindingKey> getKeys() {
        return Collections.unmodifiableSet(KEYS);
    }

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
            LOG.warn("Unexpected error", oer);
            writeOwsExceptionReport(req, res, oer);
        }
    }

    protected AbstractServiceRequest<?> parseRequest(HttpServletRequest request)
            throws OwsExceptionReport {
        return ((AbstractServiceRequest<?>)decode(request)).setRequestContext(getRequestContext(request));
    }

    @Override
    public Set<String> getConformanceClasses(String service, String version) {
        if(SosConstants.SOS.equals(service) && Sos2Constants.SERVICEVERSION.equals(version)) {
            return Collections.unmodifiableSet(CONFORMANCE_CLASSES);
        }
        return Collections.emptySet();
    }

    @Override
    public boolean checkOperationHttpPostSupported(OperationKey k) {
        return hasDecoder(k, MediaTypes.TEXT_XML) ||
               hasDecoder(k, MediaTypes.APPLICATION_XML);
    }

    @Override
    protected MediaType getDefaultContentType() {
        return MediaTypes.APPLICATION_XML;
    }

    @Override
    public Set<MediaType> getSupportedEncodings() {
        return Sets.newHashSet(MediaTypes.TEXT_XML, MediaTypes.APPLICATION_XML);
    }

    @Override
    public String getUrlPattern() {
        return BindingConstants.POX_BINDING_ENDPOINT;
    }

}

