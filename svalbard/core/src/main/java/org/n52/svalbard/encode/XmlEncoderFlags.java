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
package org.n52.svalbard.encode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public enum XmlEncoderFlags {
    /**
     * Should be of type {@code String}.
     */
    ENCODE_NAMESPACE,
    /**
     * Should be of type {@code boolean}.
     */
    ADD_SCHEMA_LOCATION,
    /**
     * Should be of type {@code String}.
     */
    XML_VERSION,
    /**
     * Should be of type {@code java.util.function.Supplier&lt;org.apache.xmlbeans.XmlOptions&gt;}.
     */
    XML_OPTIONS
}
