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
package org.n52.iceland.statistics.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.n52.iceland.statistics.api.parameters.AbstractEsParameter;

public abstract class AbstractElasticSearchDataHolder {
    protected final Map<String, Object> dataMap = new HashMap<>();

    protected Map<String, Object> put(AbstractEsParameter key,
            Object value) {
        return put(key.getName(), value);
    }

    protected Map<String, Object> put(String key, Object value) {
        if (key == null || value == null) {
            return dataMap;
        }
        // do not insert empty maps
        if (value instanceof Collection<?> && ((Collection<?>) value).isEmpty()) {
            return dataMap;
        }
        dataMap.put(key, value);
        return dataMap;
    }

}
