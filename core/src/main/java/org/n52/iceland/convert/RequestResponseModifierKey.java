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
package org.n52.iceland.convert;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import org.n52.janmayen.Comparables;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;

import com.google.common.base.Strings;


/**
 * Key class to identify {@link RequestResponseModifier}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
@SuppressWarnings("rawtypes")
public class RequestResponseModifierKey implements Comparable<RequestResponseModifierKey> {
    private final String service;
    private final String version;
    private Optional<Class<? extends OwsServiceRequest>> request;
    private Optional<Class<? extends OwsServiceResponse>> response;

    /**
     * Creates a new {@code RequestResponseModifierKey}.
     *
     * @param service The service name
     * @param version The service version
     * @param request The {@link OwsServiceRequest}
     */
    public RequestResponseModifierKey(String service, String version,
                                      OwsServiceRequest request) {
        this(service, version, request, null);
    }

    /**
     * Creates a new {@code RequestResponseModifierKey}.
     *
     * @param service  The service name
     * @param version  The service version
     * @param request  The {@link OwsServiceRequest}
     * @param response The {@link OwsServiceResponse}
     */
    public RequestResponseModifierKey(String service, String version,
                                      OwsServiceRequest request,
                                      OwsServiceResponse response) {
        this(service, version, getClass(request), getClass(response));
    }

    /**
     * Creates a new {@code RequestResponseModifierKey}.
     *
     * @param service The service name
     * @param version The service version
     * @param request The {@link OwsServiceRequest}
     */
    public RequestResponseModifierKey(String service, String version,
                                      Class<? extends OwsServiceRequest> request) {
        this(service, version, request, null);
    }

    /**
     * Creates a new {@code RequestResponseModifierKey}.
     *
     * @param service  The service name
     * @param version  The service version
     * @param request  The {@link OwsServiceRequest}
     * @param response The {@link OwsServiceResponse}
     */
    public RequestResponseModifierKey(String service, String version,
                                      Class<? extends OwsServiceRequest> request,
                                      Class<? extends OwsServiceResponse> response) {
        this(service, version, Optional.ofNullable(request), Optional
             .ofNullable(response));
    }

    /**
     * Creates a new {@code RequestResponseModifierKey}.
     *
     * @param service  The service name
     * @param version  The service version
     * @param request  The {@link OwsServiceRequest}
     * @param response The {@link OwsServiceResponse}
     */
    public RequestResponseModifierKey(String service, String version,
                                      Optional<Class<? extends OwsServiceRequest>> request,
                                      Optional<Class<? extends OwsServiceResponse>> response) {
        this.service = Optional.ofNullable(service).orElse("");
        this.version = Optional.ofNullable(version).orElse("");
        this.request = Objects.requireNonNull(request);
        this.response = Objects.requireNonNull(response);
    }

    /**
     * @return the service
     */
    public String getService() {
        return this.service;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return this.version;
    }

    public boolean isSetService() {
        return !Strings.isNullOrEmpty(getService());
    }

    public boolean isSetVersion() {
        return !Strings.isNullOrEmpty(getVersion());
    }

    /**
     * @return the request
     */
    public Class<? extends OwsServiceRequest> getRequest() {
        return this.request.orElse(null);
    }

    public boolean isSetRequest() {
        return this.request.isPresent();
    }

    /**
     * @return the response
     */
    public Class<? extends OwsServiceResponse> getResponse() {
        return this.response.orElse(null);
    }

    public boolean isSetResponse() {
        return this.response.isPresent();
    }

    @Override
    public String toString() {
        return String.format("%s[service=%s, service=%s, request=%s, response=%s]",
                             getClass().getSimpleName(), getService(), getVersion(),
                             isSetRequest() ? getRequest().getClass().getSimpleName() : "",
                             isSetResponse() ? getResponse().getClass().getSimpleName(): "");
    }

    @Override
    public int compareTo(RequestResponseModifierKey o) {
        Comparator<String> stringComparator = Comparables.allowNull(String::compareTo);
        Comparator<Class<?>> classComparator = Comparables.allowNull((a,b) -> {
            return a == b ? 0 : a.getName().compareTo(b.getName());
        });
        return Comparator
                .comparing(RequestResponseModifierKey::getService, stringComparator)
                .thenComparing(RequestResponseModifierKey::getVersion, stringComparator)
                .thenComparing(RequestResponseModifierKey::getRequest, classComparator)
                .thenComparing(RequestResponseModifierKey::getResponse, classComparator)
                .compare(this, o);
    }


    @Override
    public boolean equals(Object o) {
        if (o != null && o.getClass() == getClass()) {
            RequestResponseModifierKey other = (RequestResponseModifierKey) o;
            return Objects.equals(getService(), other.getService()) &&
                   Objects.equals(getVersion(), other.getVersion()) &&
                   Objects.equals(getRequest(), other.getRequest()) &&
                   Objects.equals(getResponse(), other.getResponse());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = Objects.hash(getService(), getVersion(), getRequest().getClass());
        if (isSetResponse()) {
            return Objects.hash(hashCode, getResponse().getClass());
        }
        return hashCode;
    }


    @SuppressWarnings("unchecked")
    private static <T> Optional<Class<? extends T>> getClass(T t) {
        return Optional.ofNullable(t).map(x -> {
            return (Class<? extends T>) x.getClass();
        });
    }

}
