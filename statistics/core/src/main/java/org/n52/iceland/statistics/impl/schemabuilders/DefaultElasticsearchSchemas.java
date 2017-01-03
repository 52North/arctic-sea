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
package org.n52.iceland.statistics.impl.schemabuilders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.n52.iceland.statistics.api.mappings.MetadataDataMapping;
import org.n52.iceland.statistics.api.mappings.ServiceEventDataMapping;
import org.n52.iceland.statistics.api.parameters.AbstractEsParameter;
import org.n52.iceland.statistics.api.parameters.ObjectEsParameter;
import org.n52.iceland.statistics.api.parameters.SingleEsParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for further application specific Elasticsearch schema
 * creation.
 *
 */
public abstract class DefaultElasticsearchSchemas {

    private static final Logger logger = LoggerFactory.getLogger(DefaultElasticsearchSchemas.class);

    protected Map<String, Object> properties;
    protected Map<String, Object> mappings;

    public final Map<String, Object> getSchema() {
        properties = new HashMap<>(1);
        mappings = new HashMap<>();
        properties.put("properties", mappings);

        processSchemaClass(ServiceEventDataMapping.class);
        appSpecificSchema();

        return properties;
    }

    /**
     * Call this method in your subclass and point it to your class where the
     * mappings exists. This class will process the
     * <code>public static final {@link AbstractEsParameter}</code> fields only.
     *
     * @param schemaClass
     *            application specific schema
     */
    protected final void processSchemaClass(Class<?> schemaClass) {
        for (Field field : schemaClass.getDeclaredFields()) {
            AbstractEsParameter value = checkField(field);
            if (value != null) {
                resolveParameterField(value, mappings);
            }
        }
        logger.debug(mappings.toString());
    }

    private void resolveParameterField(AbstractEsParameter value, Map<String, Object> map) {
        if (value instanceof SingleEsParameter) {
            SingleEsParameter single = (SingleEsParameter) value;
            map.put(single.getName(), single.getTypeAsMap());
        } else if (value instanceof ObjectEsParameter) {
            ObjectEsParameter object = (ObjectEsParameter) value;

            // loadup all the children
            // the wrapper properties map is needed to elasticsearch
            Map<String, Object> subproperties = new HashMap<>(1);
            Map<String, Object> childrenMap = new HashMap<>(value.getAllChildren().size());
            subproperties.put("properties", childrenMap);

            for (AbstractEsParameter child : object.getAllChildren()) {
                resolveParameterField(child, childrenMap);
            }

            map.put(object.getName(), subproperties);

        } else {
            throw new IllegalArgumentException("Invalid schema parameter value " + value.toString());
        }
    }

    private AbstractEsParameter checkField(Field field) {
        boolean bool = Modifier.isFinal(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers());
        bool = bool && field.getType().isAssignableFrom((AbstractEsParameter.class));
        if (bool) {
            try {
                return (AbstractEsParameter) field.get(null);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public final Map<String, Object> getMetadataSchema() {
        properties = new HashMap<>(1);
        mappings = new HashMap<>();
        properties.put("properties", mappings);
        processSchemaClass(MetadataDataMapping.class);
        return properties;
    }

    public abstract int getSchemaVersion();

    /**
     * {@link DefaultElasticsearchSchemas#processSchemaClass(Class)}
     */
    protected abstract void appSpecificSchema();
}
