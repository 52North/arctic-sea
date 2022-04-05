/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.svalbard.encode;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.n52.shetland.w3c.SchemaLocation;

/**
 *
 * @author Christian Autermann
 */
public interface SchemaAwareEncoder<T, S> extends Encoder<T, S> {

    /**
     * Add the namespace prefix of this {@linkplain Encoder} instance to the
     * given {@linkplain Map}.
     *
     * @param nameSpacePrefixMap
     *            the map
     */
    default void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        // do nothing
    }

    default Set<SchemaLocation> getSchemaLocations() {
        return Collections.emptySet();
    }

}
