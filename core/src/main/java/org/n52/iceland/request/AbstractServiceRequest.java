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
package org.n52.iceland.request;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.n52.iceland.exception.ows.concrete.MissingServiceParameterException;
import org.n52.iceland.exception.ows.concrete.MissingVersionParameterException;
import org.n52.iceland.i18n.LocaleHelper;
import org.n52.iceland.service.AbstractServiceCommunicationObject;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.shetland.ogc.ows.HasExtension;
import org.n52.shetland.ogc.ows.OWSConstants;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.ows.extension.Extensions;
import org.n52.shetland.ogc.ows.extension.Value;

import com.google.common.base.Strings;

/**
 * Abstract super class for all service request classes
 *
 * @since 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServiceRequest
        extends AbstractServiceCommunicationObject
        implements Request, HasExtension<AbstractServiceRequest> {

    private List<ServiceOperatorKey> serviceOperatorKeyTypes;
    private RequestContext requestContext;
    private Extensions extensions = new Extensions();
    private Optional<String> originalRequest = Optional.empty();

    public AbstractServiceRequest() {
    }

    public AbstractServiceRequest(String service, String version) {
        super(service, version);
    }

    public AbstractServiceRequest(String service, String version, String operationName) {
        super(service, version, operationName);
    }

    public List<ServiceOperatorKey> getServiceOperatorKeys() throws OwsExceptionReport {
        if (serviceOperatorKeyTypes == null) {
            checkServiceAndVersionParameter();
            serviceOperatorKeyTypes = Collections.singletonList(new ServiceOperatorKey(getService(), getVersion()));
        }
        return Collections.unmodifiableList(serviceOperatorKeyTypes);
    }

    private void checkServiceAndVersionParameter() throws OwsExceptionReport {
        if (!isSetService()) {
            throw new MissingServiceParameterException();
        }
        if (!isSetVersion()) {
            throw new MissingVersionParameterException();
        }
    }

    public RequestContext getRequestContext() {
        return requestContext;
    }

    public AbstractServiceRequest setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
        return this;
    }

    public boolean isSetRequestContext() {
        return requestContext != null;
    }

    @Override
    public Extensions getExtensions() {
        return extensions;
    }

    @Override
    public AbstractServiceRequest setExtensions(Extensions extensions) {
        this.extensions = Optional.ofNullable(extensions).orElseGet(Extensions::new);
        return this;
    }

    public boolean isSetRequestedLanguage() {
        return !Strings.isNullOrEmpty(getRequestedLanguage());
    }

    public String getRequestedLanguage() {
        return getExtension(OWSConstants.AdditionalRequestParams.language).map(Extension::getValue)
                .map(value -> {
                    if (value instanceof Value<?, ?>) {
                        return ((Value<?, ?>) value).getStringValue();
                    } else if (value instanceof String) {
                        return (String) value;
                    } else {
                        return "";
                    }
                }).orElse("");
    }

    public Locale getRequestedLocale() {
        return LocaleHelper.fromString(getRequestedLanguage());
    }

    public Optional<String> getOriginalRequest() {
        return originalRequest;
    }

    public void setOriginalRequest(String request) {
        this.originalRequest = Optional.ofNullable(request);
    }

    @Override
    public String toString() {
        return String.format("%s[service=%s, version=%s, operation=%s]", getClass().getName(), getService(),
                getVersion(), getOperationName());
    }
}
