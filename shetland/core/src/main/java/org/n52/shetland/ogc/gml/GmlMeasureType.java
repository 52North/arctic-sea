/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.gml;

import org.n52.shetland.ogc.AbstractMeasureType;

/**
 * Class represents a GML conform MeasureType element
 *
 * @since 1.0.0
 *
 */
public class GmlMeasureType extends AbstractMeasureType {

    /**
     * constructor
     *
     * @param value
     *            Measured value
     */
    public GmlMeasureType(Double value) {
        super(value, null);
    }

    /**
     * constructor
     *
     * @param value
     *            Measured value
     * @param unit
     *            Unit of measure
     */
    public GmlMeasureType(Double value, String unit) {
        super(value, unit);
    }

}
