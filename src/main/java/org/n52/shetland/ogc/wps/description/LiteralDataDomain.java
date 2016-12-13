/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps.description;

import java.net.URI;
import java.util.Optional;

import org.n52.shetland.ogc.ows.OwsDomainMetadata;
import org.n52.shetland.ogc.ows.OwsPossibleValues;
import org.n52.shetland.ogc.ows.OwsValue;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface LiteralDataDomain {
    OwsPossibleValues getPossibleValues();

    Optional<OwsDomainMetadata> getDataType();

    Optional<OwsDomainMetadata> getUOM();

    Optional<OwsValue> getDefaultValue();

    interface Builder<T extends LiteralDataDomain, B extends Builder<T, B>> extends org.n52.janmayen.Builder<T, B> {
        B withDefaultValue(OwsValue value);

        default B withDefaultValue(String value) {
            return withDefaultValue(new OwsValue(value));
        }

        B withDataType(OwsDomainMetadata dataType);

        default B withDataType(URI reference, String value) {
            return withDataType(Strings.emptyToNull(value) == null ? null
                                        : new OwsDomainMetadata(reference, value));
        }

        default B withDataType(String value) {
            return withDataType(Strings.emptyToNull(value) == null ? null
                                        : new OwsDomainMetadata(value));
        }

        B withUOM(OwsDomainMetadata uom);

        default B withUOM(URI reference, String value) {
            return withUOM(Strings.emptyToNull(value) == null ? null
                                   : new OwsDomainMetadata(reference, value));
        }

        default B withUOM(String value) {
            return withUOM(Strings.emptyToNull(value) == null ? null
                                   : new OwsDomainMetadata(value));
        }

        B withValueDescription(OwsPossibleValues allowedValues);

    }
}
