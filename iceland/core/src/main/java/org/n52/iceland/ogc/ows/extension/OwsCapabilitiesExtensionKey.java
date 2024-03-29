/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.iceland.ogc.ows.extension;

import java.util.Objects;

import org.n52.shetland.ogc.ows.OwsCapabilitiesExtension;

import com.google.common.collect.ComparisonChain;


/**
 * {@link OwsCapabilitiesExtension} key class to identify CapabilitiesExtensions.
 *
 * @since 1.0.0
 *
 */
public class OwsCapabilitiesExtensionKey implements Comparable<OwsCapabilitiesExtensionKey> {
    private String service;

    private String version;

    /**
     * Default constructor
     */
    public OwsCapabilitiesExtensionKey() {
        this(null, null);
    }

    /**
     * Constructor
     *
     * @param service
     *            Related service
     * @param version
     *            Related version
     */
    public OwsCapabilitiesExtensionKey(String service, String version) {
        this.service = service;
        this.version = version;
    }

    /**
     * Get the key service
     *
     * @return Key servcice
     */
    public String getService() {
        return service;
    }

    /**
     * Set the key service
     *
     * @param service
     *            service to set
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * Get the key version
     *
     * @return Key version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the key version
     *
     * @param version
     *            version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int compareTo(OwsCapabilitiesExtensionKey o) {
        return ComparisonChain.start()
                .compare(getService(), o.getService())
                .compare(getVersion(), o.getVersion())
                .result();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object paramObject) {
        if (service != null && version != null && paramObject instanceof OwsCapabilitiesExtensionKey) {
            OwsCapabilitiesExtensionKey toCheck = (OwsCapabilitiesExtensionKey) paramObject;
            return Objects.equals(getService(), toCheck.getService()) &&
                   Objects.equals(getVersion(), toCheck.getVersion());
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(getService(), getVersion());
    }

    @Override
    public String toString() {
        return String.format("CapabilitiesExtensionKey[service=%s, version=%s]", this.service, this.version);
    }
}
