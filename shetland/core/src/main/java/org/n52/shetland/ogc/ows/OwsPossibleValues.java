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
package org.n52.shetland.ogc.ows;

/**
 * Specifies the possible values of this quantity.
 *
 * @author Christian Autermann
 */
public interface OwsPossibleValues {

    default boolean isAllowedValues() {
        return false;
    }

    default boolean isAnyValue() {
        return false;
    }

    default boolean isValuesReference() {
        return false;
    }

    default boolean isNoValues() {
        return false;
    }

    default OwsAllowedValues asAllowedValues() {
        throw new UnsupportedOperationException();
    }

    default OwsAnyValue asAnyValues() {
        throw new UnsupportedOperationException();
    }

    default OwsValuesReference asValuesReference() {
        throw new UnsupportedOperationException();
    }

    default OwsNoValues asNoValues() {
        throw new UnsupportedOperationException();
    }
}
