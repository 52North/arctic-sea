/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.shetland.w3c;

import java.util.Comparator;

import com.google.common.base.Objects;

/**
 * Class represents a XML schema location with namespace and schema file URL.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 */
public class SchemaLocation implements Comparable<SchemaLocation> {
    private static final Comparator<SchemaLocation> COMPARATOR
            = Comparator.comparing(SchemaLocation::getNamespace)
                    .thenComparing(SchemaLocation::getSchemaFileUrl);
    private final String namespace;
    private final String schemaFileUrl;
    private final String schemaLocationString;

    /**
     * Constructor
     *
     * @param namespace Namespace
     * @param schemaFileUrl Schema file URL
     */
    public SchemaLocation(String namespace, String schemaFileUrl) {
        this.namespace = namespace;
        this.schemaFileUrl = schemaFileUrl;
        this.schemaLocationString = String.format("%s %s", namespace, schemaFileUrl);
    }

    /**
     * Get namespace of schema location
     *
     * @return namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Get schema file URL
     *
     * @return schema file URL
     */
    public String getSchemaFileUrl() {
        return schemaFileUrl;
    }

    /**
     * @return Schema location string
     */
    public String getSchemaLocationString() {
        return schemaLocationString;
    }

    @Override
    public int compareTo(SchemaLocation other) {
        return COMPARATOR.compare(this, other);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getNamespace(), getSchemaFileUrl());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == getClass()) {
            SchemaLocation other = (SchemaLocation) obj;
            return Objects.equal(getNamespace(), other.getNamespace()) &&
                     Objects.equal(getSchemaFileUrl(), other.getSchemaFileUrl());
        }
        return false;
    }
}
