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
package org.n52.iceland.ogc;

import java.util.Comparator;
import java.util.Objects;

import org.n52.shetland.ogc.ows.service.OwsServiceKey;


/**
 * Abstract class for comparable keys with parameters service, version and domain
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 * @param <T> implementation of this class
 */
public abstract class AbstractComparableServiceVersionDomainKey<T extends AbstractComparableServiceVersionDomainKey<T>>
        implements Comparable<T> {
    private OwsServiceKey sok;

    private String domain;

    /**
     * constructor
     *
     * @param sok the {@link OwsServiceKey} to set
     * @param domain the domain to set
     */
    public AbstractComparableServiceVersionDomainKey(OwsServiceKey sok, String domain) {
        setServiceOperatorKey(sok);
        setDomain(domain);
    }

    /**
     * constructor
     *
     * @param service the service to set
     * @param version the version to set
     * @param domain the domain to set
     */
    public AbstractComparableServiceVersionDomainKey(String service, String version, String domain) {
        this(new OwsServiceKey(service, version), domain);
    }

    /**
     * Set the {@link OwsServiceKey} to set
     *
     * @param sok the {@link OwsServiceKey} to set
     */
    private void setServiceOperatorKey(OwsServiceKey sok) {
        this.sok = sok;
    }

    /**
     * Get the {@link OwsServiceKey}
     *
     * @return the {@link OwsServiceKey}
     */
    public OwsServiceKey getServiceOperatorKey() {
        return sok;
    }

    /**
     * Get the service
     *
     * @return the service
     */
    public String getService() {
        return sok == null ? null : sok.getService();
    }

    /**
     * Get the version
     *
     * @return the version
     */
    public String getVersion() {
        return sok == null ? null : sok.getVersion();
    }

    /**
     * Get the domain
     *
     * @return the domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Set the domain
     *
     * @param domain the domain to set
     */
    private void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(T other) {
        return Comparator.comparing(T::getServiceOperatorKey)
                .thenComparing(T::getDomain)
                .compare((T)this, other);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            T o = (T) obj;
            return Objects.equals(getServiceOperatorKey(), o.getServiceOperatorKey()) &&
                     Objects.equals(getDomain(), o.getDomain());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServiceOperatorKey(), getDomain());
    }

    @Override
    public String toString() {
        return String.format("%s[serviceOperatorKeyType=%s, domain=%s]", getClass().getSimpleName(),
                             getServiceOperatorKey(), getDomain());
    }
}
