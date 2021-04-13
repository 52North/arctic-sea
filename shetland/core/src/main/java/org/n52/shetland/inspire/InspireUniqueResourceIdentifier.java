/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire;

import com.google.common.base.Strings;

/**
 * Service internal representation of INSPIRE unique resource identifier
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class InspireUniqueResourceIdentifier implements InspireObject {

    /* Element Code 1..1 */
    private String code;

    /* Element Namespace 0..1 */
    private String namespace;

    /* Attribute metadataURL */
    private String metadataUrl;

    /**
     * constructor
     *
     * @param code
     *            the code of the identifier
     */
    public InspireUniqueResourceIdentifier(String code) {
        this.code = code;
    }

    /**
     * Get the code
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the namespace
     *
     * @return the namespaces
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Set the namespace
     *
     * @param namespace
     *            the namespace to set
     * @return this
     */
    public InspireUniqueResourceIdentifier setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * Check if the namespace is set
     *
     * @return <code>true</code>, if the namespace is set
     */
    public boolean isSetNamespace() {
        return !Strings.isNullOrEmpty(getNamespace());
    }

    /**
     * Get the metadata URL
     *
     * @return the metadataUrl
     */
    public String getMetadataUrl() {
        return metadataUrl;
    }

    /**
     * Set the metadata URL
     *
     * @param metadataUrl
     *            the metadataUrl to set
     * @return this
     */
    public InspireUniqueResourceIdentifier setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
        return this;
    }

    /**
     * Check if the metadata URL is set
     *
     * @return <code>true</code>, if the metadata URL is set
     */
    public boolean isSetMetadataUrl() {
        return !Strings.isNullOrEmpty(getMetadataUrl());
    }

}
