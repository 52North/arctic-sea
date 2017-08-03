/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

/**
 * Specifies that no values are allowed for this parameter or quantity.
 *
 * @author Christian Autermann
 */
public final class OwsNoValues
        implements OwsPossibleValues {

    private static final OwsNoValues INSTANCE = new OwsNoValues();

    private OwsNoValues() {
    }

    @Override
    public boolean isNoValues() {
        return true;
    }

    @Override
    public OwsNoValues asNoValues() {
        return this;
    }

    @Override
    public String toString() {
        return "OwsNoValues{}";
    }

    public static OwsNoValues instance() {
        return INSTANCE;
    }
}
