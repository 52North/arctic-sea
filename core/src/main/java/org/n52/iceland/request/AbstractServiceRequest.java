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

import org.n52.iceland.exception.ows.InvalidParameterValueException;
import org.n52.iceland.exception.ows.OwsExceptionReport;
import org.n52.iceland.exception.ows.concrete.MissingServiceParameterException;
import org.n52.iceland.exception.ows.concrete.MissingVersionParameterException;
import org.n52.iceland.i18n.LocaleHelper;
import org.n52.iceland.ogc.ows.OWSConstants;
import org.n52.iceland.ogc.ows.OWSConstants.HasExtension;
import org.n52.iceland.ogc.ows.extension.Extension;
import org.n52.iceland.ogc.ows.extension.Extensions;
import org.n52.iceland.ogc.ows.extension.Value;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.iceland.service.AbstractServiceCommunicationObject;
import org.n52.iceland.service.operator.ServiceOperatorKey;
import org.n52.iceland.util.Constants;
import org.n52.iceland.util.StringHelper;

/**
 * Abstract super class for all service request classes
 *
 * @since 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractServiceRequest<T extends AbstractServiceResponse>
        extends AbstractServiceCommunicationObject
        implements Request, HasExtension<AbstractServiceRequest> {

    private List<ServiceOperatorKey> serviceOperatorKeyTypes;

    private RequestContext requestContext;

    private Extensions extensions;

    private Optional<String> originalRequest = Optional.empty();

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

    public AbstractServiceRequest setRequestContext(final RequestContext requestContext) {
        this.requestContext = requestContext;
        return this;
    }

    public boolean isSetRequestContext() {
        return requestContext != null;
    }

    public abstract T getResponse() throws OwsExceptionReport;

    @Override
    public Extensions getExtensions() {
        return extensions;
    }

    @Override
    public AbstractServiceRequest setExtensions(final Extensions extensions) {
        this.extensions = extensions;
        return this;
    }

    @Override
    public AbstractServiceRequest addExtensions(final Extensions extensions) {
        if (getExtensions() == null) {
            setExtensions(extensions);
        } else {
            getExtensions().addExtension(extensions.getExtensions());
        }
        return this;
    }

    @Override
    public AbstractServiceRequest addExtension(final Extension extension) {
        if (getExtensions() == null) {
            setExtensions(new Extensions());
        }
        getExtensions().addExtension(extension);
        return this;
    }

    @Override
    public boolean isSetExtensions() {
        return extensions != null && !extensions.isEmpty();
    }

    @Override
    public boolean hasExtension(Enum identifier) {
        if (isSetExtensions()) {
            return getExtensions().containsExtension(identifier);
        }
        return false;
    }

    @Override
    public boolean hasExtension(String identifier) {
        if (isSetExtensions()) {
            return getExtensions().containsExtension(identifier);
        }
        return false;
    }

    @Override
    public Extension<?> getExtension(Enum identifier) throws InvalidParameterValueException {
        if (hasExtension(identifier)) {
            return getExtensions().getExtension(identifier);
        }
        return null;
    }

    @Override
    public Extension<?> getExtension(String identifier) throws InvalidParameterValueException {
        if (hasExtension(identifier)) {
            return getExtensions().getExtension(identifier);
        }
        return null;
    }

    public boolean isSetRequestedLanguage() {
        return StringHelper.isNotEmpty(getRequestedLanguage());
    }

    public String getRequestedLanguage() {
        if (isSetExtensions()) {
            if (getExtensions().containsExtension(OWSConstants.AdditionalRequestParams.language)) {
                Object value = getExtensions().getExtension(OWSConstants.AdditionalRequestParams.language).getValue();
                if (value instanceof Value<?, ?>) {
                    return ((Value<?, ?>) value).getStringValue();
                }
                if (value instanceof String) {
                    return (String) value;
                }
            }
        }
        return Constants.EMPTY_STRING;
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
