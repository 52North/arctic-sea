/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
    private static final String TYPE = "type";
    private static final String INDEX = "index";
    private static final String STRING_TYPE = "string";
    public static final ElasticsearchType STRING_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, STRING_TYPE, INDEX, "not_analyzed"));
    public static final ElasticsearchType STRING_ANALYZED_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, STRING_TYPE, INDEX, "analyzed"));
    public static final ElasticsearchType DATE_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, "date"));
    public static final ElasticsearchType INTEGER_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, "integer"));
    public static final ElasticsearchType LONG_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, "long"));
    public static final ElasticsearchType DOUBLE_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, "double"));
    public static final ElasticsearchType BOOLEAN_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, "boolean"));
    public static final ElasticsearchType IPV4_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, "ip"));
    public static final ElasticsearchType GEO_POINT_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, "geo_point"));
    public static final ElasticsearchType GEO_SHAPE_FIELD = new ElasticsearchType(ImmutableMap
            .<String, Object>of(TYPE, "geo_shape", "precision", "1km"));

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
            return type.get(TYPE).toString();
        }
    }

}
