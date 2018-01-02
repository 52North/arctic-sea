/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import java.util.List;
import java.util.Map;

import org.n52.iceland.statistics.api.parameters.ElasticsearchTypeRegistry.ElasticsearchType;

/**
 * Abstract Elasticsearch variables which the user sees on the interface by the name
 */
public abstract class AbstractEsParameter {
    private final String name;
    private Description description;
    private ElasticsearchType type;

    public AbstractEsParameter(String name) {
        this.name = name;
    }

    public AbstractEsParameter(String name, Description description) {
        this.name = name;
        this.description = description;
    }

    public final Description getDescription() {
        return description;
    }

    public final void setDescription(Description description) {
        this.description = description;
    }

    public boolean hasDescription() {
        return this.description != null;
    }

    public final String getName() {
        return name;
    }

    public List<AbstractEsParameter> getAllChildren() {
        return null;
    }

    public ElasticsearchType getType() {
        return type;
    }

    public Map<String, Object> getTypeAsMap() {
        if (type != null) {
            return type.getType();
        } else {
            return null;
        }
    }

    public void setType(ElasticsearchType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AbstractEsParameter [name=" + name + ", description=" + description + ", type=" + type + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        AbstractEsParameter other = (AbstractEsParameter) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }

}
