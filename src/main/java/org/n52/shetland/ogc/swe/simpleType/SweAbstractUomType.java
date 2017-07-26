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
package org.n52.shetland.ogc.swe.simpleType;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public abstract class SweAbstractUomType<T> extends SweAbstractSimpleType<T> {

    /**
     * unit of measurement
     */
    private UoM uom;

    /**
     * Get unit of measurement
     *
     * @return the uom
     */
    public String getUom() {
        if (uom != null) {
            return uom.getUom();
        }
        return "";
    }

    /**
     * Get unit of measurement object
     *
     * @return the uom
     */
    public UoM getUomObject() {
        return uom;
    }

    /**
     * Set unit of measurement
     *
     * @param uom
     *            the uom to set
     * @return This SweAbstractUomType
     */
    public SweAbstractUomType<T> setUom(final String uom) {
        this.uom = new UoM(uom);
        return this;
    }

    /**
     * Set unit of measurement
     *
     * @param uom
     *            the uom to set
     * @return This SweAbstractUomType
     */
    public SweAbstractUomType<T> setUom(final UoM uom) {
        this.uom = uom;
        return this;
    }

    /**
     *
     * @return <tt>true</tt>, if the uom is set and not an empty string,<br>
     *         <tt>false</tt>, else.
     */
    public boolean isSetUom() {
        return uom != null && !uom.isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s [simpleType=%s, value=%s, uom=%s, quality=%s]", getClass().getSimpleName(),
                getDataComponentType(), getValue(), getUom(), getQuality());
    }

    @Override
    public SweAbstractDataComponent copyValueTo(SweAbstractDataComponent copy) {
        if (copy instanceof SweAbstractUomType<?>) {
            ((SweAbstractUomType<?>) copy).setUom(getUom());
        }
        return super.copyValueTo(copy);
    }
}
