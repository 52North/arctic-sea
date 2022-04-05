/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.iceland.binding.AbstractXmlBinding;
import org.n52.iceland.binding.Binding;
import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.binding.MediaTypeBindingKey;
import org.n52.iceland.exception.HTTPException;
import org.n52.iceland.service.MiscSettings;
import org.n52.janmayen.http.MediaType;
import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

/**
 * {@link Binding} implementation for POX (XML) encoded requests
 *
 * @since 1.0.0
 *
 */
@Configurable
public class PoxBinding extends AbstractXmlBinding<OwsServiceRequest> {

    private static final Set<BindingKey> KEYS = ImmutableSet.<BindingKey>builder()
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
        OwsServiceRequest request = null;
        try {
            request = parseRequest(req);
            OwsServiceResponse response = getServiceOperator(request)
                    .receiveRequest(request);
            writeResponse(req, res, response);
        } catch (OwsExceptionReport oer) {
            oer.setVersion(request != null ? request.getVersion() : null);
            LOG.warn("Unexpected error", oer);
            writeOwsExceptionReport(req, res, oer);
        }
    }

    protected OwsServiceRequest parseRequest(HttpServletRequest request) throws OwsExceptionReport {
        return decode(request).setRequestContext(getRequestContext(request));
    }

    @Override
    public boolean checkOperationHttpPostSupported(OwsOperationKey k) {
        return hasDecoder(k, MediaTypes.TEXT_XML) ||
               hasDecoder(k, MediaTypes.APPLICATION_XML);
    }

    @Override
    protected MediaType getDefaultContentType() {
        return MediaTypes.APPLICATION_XML;
    }

}
