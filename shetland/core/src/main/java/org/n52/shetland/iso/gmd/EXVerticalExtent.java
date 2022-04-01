/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso.gmd;

import org.n52.shetland.w3c.Nillable;
import org.n52.shetland.w3c.xlink.Referenceable;

/**
 * Internal representation of the ISO GMD ExVerticalExtent.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class EXVerticalExtent
        extends AbstractObject {

    /* 1..1 */
    private Nillable<Double> minimumValue;
    /* 1..1 */
    private Nillable<Double> maximumValue;
    /* 1..1 */
    private Referenceable<ScCRS> verticalCRS;

    /**
     * @return the minimumValue
     */
    public Nillable<Double> getMinimumValue() {
        return minimumValue;
    }

    /**
     * @param minimumValue
     *            the minimumValue to set
     */
    public void setMinimumValue(Nillable<Double> minimumValue) {
        this.minimumValue = minimumValue;
    }

    /**
     * @return the maximumValue
     */
    public Nillable<Double> getMaximumValue() {
        return maximumValue;
    }

    /**
     * @param maximumValue
     *            the maximumValue to set
     */
    public void setMaximumValue(Nillable<Double> maximumValue) {
        this.maximumValue = maximumValue;
    }

    /**
     * @return the verticalCRS
     */
    public Referenceable<ScCRS> getVerticalCRS() {
        return verticalCRS;
    }

    /**
     * @param verticalCRS
     *            the verticalCRS to set
     */
    public void setVerticalCRS(Referenceable<ScCRS> verticalCRS) {
        this.verticalCRS = verticalCRS;
    }
}
