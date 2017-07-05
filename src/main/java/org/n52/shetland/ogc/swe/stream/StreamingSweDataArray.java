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
package org.n52.shetland.ogc.swe.stream;

import org.n52.shetland.ogc.om.StreamingValue;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;
import org.n52.shetland.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.shetland.ogc.swe.simpleType.SweCount;

public class StreamingSweDataArray extends SweAbstractDataComponent {

    /**
     * swe:values<br />
     * Each list entry represents one block, a list of tokens.<br />
     * Atm, this implementation using java.lang.String to represent each token.
     */
    private StreamingValue values;

    /**
     * swe:elementType
     */
    private SweAbstractDataComponent elementType;

    /**
     *
     */
    private SweAbstractEncoding encoding;

    private SweCount elementCount;

    /**
     * @return the values
     */
    public StreamingValue getValues() {
        return values;
    }

    /**
     *
     * @param values
     *               the values to set
     *
     * @return This SweDataArray
     */
    public StreamingSweDataArray setValues(final StreamingValue values) {
        this.values = values;
        return this;
    }

    /**
     * @return the elementType
     */
    public SweAbstractDataComponent getElementType() {
        return elementType;
    }

    /**
     * @param elementType
     *                    the elementType to set
     *
     * @return This SweDataArray
     */
    public StreamingSweDataArray setElementType(
            final SweAbstractDataComponent elementType) {
        this.elementType = elementType;
        return this;
    }

    public SweCount getElementCount() {
        return new SweCount();
    }

    public SweAbstractEncoding getEncoding() {
        return encoding;
    }

    public StreamingSweDataArray setEncoding(final SweAbstractEncoding encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * @return <tt>true</tt>, if the values field is set properly
     */
    public boolean isSetValues() {
        return getValues() != null;
    }

    public boolean isSetElementTyp() {
        return elementType != null;
    }

    public boolean isSetEncoding() {
        return encoding != null;
    }

    public StreamingSweDataArray setElementCount(final SweCount elementCount) {
        this.elementCount = elementCount;
        return this;
    }

    public boolean isSetElementCount() {
        return elementCount != null || isSetValues();
    }

    public boolean isEmpty() {
        return isSetElementTyp() && isSetEncoding() && isSetValues();
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.DataArray;
    }

    @Override
    public SweAbstractDataComponent copy() {
        StreamingSweDataArray copy = new StreamingSweDataArray();
        copyValueTo(copy);
        if (isSetElementTyp()) {
            copy.setElementType(getElementType().copy());
        }
        if (isSetElementCount()) {
            copy.setElementCount(getElementCount().copy());
        }
        if (isSetEncoding()) {
            copy.setEncoding(getEncoding().copy());
        }
        if (isSetValues()) {
            copy.setValues(getValues());
        }
        return copy;
    }

    @Override
    public <T, X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public <X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X {
        visitor.visit(this);
    }

}
