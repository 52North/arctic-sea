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
package org.n52.shetland.ogc;

import com.google.common.base.Strings;

public interface PhenomenonNameDescriptionProvider {

    default boolean isSetObservablePropertyName(String observableProperty) {
        return !Strings.isNullOrEmpty(getObservablePropertyName(observableProperty));
    }

    String getObservablePropertyName(String observableProperty);

    default boolean isSetObservablePropertyDescription(String observableProperty) {
        return !Strings.isNullOrEmpty(getObservablePropertyDescription(observableProperty));
    }

    String getObservablePropertyDescription(String observableProperty);

    default boolean isSetObservablePropertyUnit(String observableProperty) {
        return getObservablePropertyUnit(observableProperty) != null;
    }

    UoM getObservablePropertyUnit(String observableProperty);
}
