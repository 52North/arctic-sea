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
package org.n52.iceland.statistics.api.parameters;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class ElasticsearchTypeRegistry {
    public final static ElasticsearchType stringField =
            new ElasticsearchType(ImmutableMap.<String, Object> of("type", "string", "index", "not_analyzed"));
    public final static ElasticsearchType stringAnalyzedField =
            new ElasticsearchType(ImmutableMap.<String, Object> of("type", "string", "index", "analyzed"));
    public final static ElasticsearchType dateField = new ElasticsearchType(ImmutableMap.<String, Object> of("type", "date"));
    public final static ElasticsearchType integerField = new ElasticsearchType(ImmutableMap.<String, Object> of("type", "integer"));
    public final static ElasticsearchType longField = new ElasticsearchType(ImmutableMap.<String, Object> of("type", "long"));
    public final static ElasticsearchType doubleField = new ElasticsearchType(ImmutableMap.<String, Object> of("type", "double"));
    public final static ElasticsearchType booleanField = new ElasticsearchType(ImmutableMap.<String, Object> of("type", "boolean"));
    public final static ElasticsearchType ipv4Field = new ElasticsearchType(ImmutableMap.<String, Object> of("type", "ip"));
    public final static ElasticsearchType geoPointField = new ElasticsearchType(ImmutableMap.<String, Object> of("type", "geo_point"));
    public final static ElasticsearchType geoShapeField =
            new ElasticsearchType(ImmutableMap.<String, Object> of("type", "geo_shape", "precision", "1km"));

    public static class ElasticsearchType {
        private final Map<String, Object> type;

        public ElasticsearchType(Map<String, Object> type) {
            this.type = type;
        }

        public Map<String, Object> getType() {
            return type;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            ElasticsearchType other = (ElasticsearchType) obj;
            if (type == null) {
                if (other.type != null) {
                    return false;
                }
            } else if (!type.equals(other.type)) {
                return false;
            }
            return true;
        }

        public String humanReadableType() {
            return type.get("type").toString();
        }
    }

}
