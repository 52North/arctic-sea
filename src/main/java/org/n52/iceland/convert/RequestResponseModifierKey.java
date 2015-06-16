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
package org.n52.iceland.convert;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;

import org.n52.iceland.request.AbstractServiceRequest;
import org.n52.iceland.response.AbstractServiceResponse;
import org.n52.iceland.util.Constants;
import org.n52.iceland.util.StringHelper;

import com.google.common.collect.ComparisonChain;

/**
 * Key class to identify {@link RequestResponseModifier}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class RequestResponseModifierKey implements Comparable<RequestResponseModifierKey> {
    private final String service;
    private final String version;
    private Optional<Class<? extends AbstractServiceRequest>> request;
    private Optional<Class<? extends AbstractServiceResponse>> response;

    /**
     * Constructor
     *
     * @param service
     *            The service name
     * @param version
     *            The service version
     * @param request
     *            The {@link AbstractServiceRequest}
     */
    public RequestResponseModifierKey(String service, String version, AbstractServiceRequest<?> request) {
        this(service, version, request, null);
    }

    /**
     * Constructor
     *
     * @param service
     *            The service name
     * @param version
     *            The service version
     * @param request
     *            The {@link AbstractServiceRequest}
     * @param response
     *            The {@link AbstractServiceResponse}
     */
    public RequestResponseModifierKey(String service, String version, AbstractServiceRequest<?> request,
            AbstractServiceResponse response) {
        this(service, version, getClass(request), getClass(response));
    }

    /**
     * Constructor
     *
     * @param service
     *            The service name
     * @param version
     *            The service version
     * @param request
     *            The {@link AbstractServiceRequest}
     */
    public RequestResponseModifierKey(String service, String version, Class<? extends AbstractServiceRequest<?>> request) {
        this(service, version, request, null);
    }

    /**
     * Constructor
     *
     * @param service
     *            The service name
     * @param version
     *            The service version
     * @param request
     *            The {@link AbstractServiceRequest}
     * @param response
     *            The {@link AbstractServiceResponse}
     */
    public RequestResponseModifierKey(String service, String version, Class<? extends AbstractServiceRequest<?>> request, Class<? extends AbstractServiceResponse> response) {
        this(service, version, Optional.ofNullable(request), Optional.ofNullable(response));
    }

     /**
     * Constructor
     *
     * @param service
     *            The service name
     * @param version
     *            The service version
     * @param request
     *            The {@link AbstractServiceRequest}
     * @param response
     *            The {@link AbstractServiceResponse}
     */
    public RequestResponseModifierKey(String service, String version, Optional<Class<? extends AbstractServiceRequest>> request, Optional<Class<? extends AbstractServiceResponse>> response) {
        this.service = Optional.ofNullable(service).orElse(Constants.EMPTY_STRING);
        this.version = Optional.ofNullable(version).orElse(Constants.EMPTY_STRING);
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

    /**
     * @return
     */
    public boolean isSetService() {
        return StringHelper.isNotEmpty(getService());
    }

    /**
     * @return
     */
    public boolean isSetVersion() {
        return StringHelper.isNotEmpty(getVersion());
    }

    /**
     * @return the request
     */
    public Class<? extends AbstractServiceRequest> getRequest() {
        return this.request.orElse(null);
    }

    /**
     * @return
     */
    public boolean isSetRequest() {
        return this.request.isPresent();
    }

    /**
     * @return the response
     */
    public Class<? extends AbstractServiceResponse> getResponse() {
        return this.response.orElse(null);
    }

    /**
     * @return
     */
    public boolean isSetResponse() {
        return this.response.isPresent();
    }

    @Override
    public String toString() {
        return String.format("%s[service=%s, service=%s, request=%s, response=%s]", getClass().getSimpleName(),
                getService(), getVersion(), isSetRequest() ? getRequest().getClass().getSimpleName()
                        : Constants.EMPTY_STRING, isSetResponse() ? getResponse().getClass().getSimpleName()
                        : Constants.EMPTY_STRING);
    }

    @Override
    public int compareTo(RequestResponseModifierKey o) {
        Comparator<String> stringComparator = nullCapabableComparator(String::compareTo);
        Comparator<Class<?>> classComparator = nullCapabableComparator((a,b) -> {
            return a == b ? 0 : a.getName().compareTo(b.getName());
        });
        return ComparisonChain.start()
                .compare(getService(), o.getService(), stringComparator)
                .compare(getVersion(), o.getVersion(), stringComparator)
                .compare(getRequest(), o.getRequest(), classComparator)
                .compare(getResponse(), o.getResponse(), classComparator)
                .result();
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

    private static <T> Comparator<T> nullCapabableComparator(BiFunction<T, T, Integer> delegate) {
        return (a,b) -> {
            if (a == null) {
                return b == null ? 0 : -1;
            } else {
                return b == null ? 1 : delegate.apply(a, b);
            }
        };
    }

}
