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
package org.n52.shetland.ogc.swe;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.encoding.SweAbstractEncoding;
import org.n52.shetland.ogc.swe.encoding.SweTextEncoding;
import org.n52.shetland.ogc.swe.simpleType.SweCount;

import com.google.common.collect.Lists;


public class SweDataArray extends SweAbstractDataComponent {

    /**
     * swe:values<br />
     * Each list entry represents one block, a list of tokens.<br />
     * Atm, this implementation using java.lang.String to represent each token.
     */
    private List<List<String>> values;

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
    public List<List<String>> getValues() {
        return values;
    }

    /**
     *
     * @param values
     *            the values to set
     * @return This SweDataArray
     */
    public SweDataArray setValues(final List<List<String>> values) {
        this.values = values;
        return this;
    }

    public String getValueAsString() {
        String blockSeparator = "@";
        String tokenSeparator = "#";
        if (isSetEncoding() && getEncoding() instanceof SweTextEncoding) {
            blockSeparator = ((SweTextEncoding) getEncoding()).getBlockSeparator();
            tokenSeparator = ((SweTextEncoding) getEncoding()).getTokenSeparator();
        }
        StringBuilder data = new StringBuilder();
        for (List<String> list : getValues()) {
            data.append(list.stream().collect(Collectors.joining(tokenSeparator))).append(blockSeparator);
        }
        return data.toString().substring(0, data.toString().length() - 1);
    }

    /**
     * @return the elementType
     */
    public SweAbstractDataComponent getElementType() {
        return elementType;
    }

    /**
     * @param elementType
     *            the elementType to set
     * @return This SweDataArray
     */
    public SweDataArray setElementType(final SweAbstractDataComponent elementType) {
        this.elementType = elementType;
        return this;
    }

    public SweCount getElementCount() {
        if (isSetValues()) {
            return new SweCount().setValue(values.size());
        } else if (isSetElementCount()) {
            return this.elementCount;
        }
        return new SweCount().setValue(0);
    }

    public SweAbstractEncoding getEncoding() {
        return encoding;
    }

    public SweDataArray setEncoding(final SweAbstractEncoding encoding) {
        this.encoding = encoding;
        return this;
    }

    /**
     * @return <tt>true</tt>, if the values field is set properly
     */
    public boolean isSetValues() {
        if (values != null && !values.isEmpty()) {
            if (values.size() == 1) {
                final List<String> list = values.get(0);
                return list != null && !list.isEmpty();
            }
            return true;
        }
        return false;
    }

    /**
     * Adds the given block - a {@link List}&lt;{@link String}&gt; - add the end of
     * the current list of blocks
     *
     * @param blockOfTokensToAddAtTheEnd the blocks of tokens to add
     * @return <tt>true</tt> (as specified by {@link Collection#add}) <br />
     *         <tt>false</tt> if block could not be added
     */
    public boolean add(final List<String> blockOfTokensToAddAtTheEnd) {
        if (values == null) {
            values = new LinkedList<>();
        }
        return values.add(blockOfTokensToAddAtTheEnd);
    }

    public boolean addAll(List<List<String>> newValues) {
        if (values == null) {
            values = newValues;
        }
        return values.addAll(newValues);
    }

    @Override
    public int hashCode() {
        final int prime = 23;
        int hash = 7;
        hash = prime * hash + super.hashCode();
        hash = prime * hash + (getValues() != null ? getValues().hashCode() : 0);
        hash = prime * hash + (getElementType() != null ? getElementType().hashCode() : 0);
        hash = prime * hash + (getEncoding() != null ? getEncoding().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SweDataArray other = (SweDataArray) obj;
        if (getValues() != other.getValues() && (getValues() == null || !getValues().equals(other.getValues()))) {
            return false;
        }
        if (getElementType() != other.getElementType()
                && (getElementType() == null || !getElementType().equals(other.getElementType()))) {
            return false;
        }
        if (getEncoding() != other.getEncoding()
                && (getEncoding() == null || !getEncoding().equals(other.getEncoding()))) {
            return false;
        }
        return super.equals(obj);
    }

    public boolean isSetElementTyp() {
        return elementType != null;
    }

    public boolean isSetEncoding() {
        return encoding != null;
    }

    public SweDataArray setElementCount(final SweCount elementCount) {
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
    public <T, X extends Throwable> T accept(SweDataComponentVisitor<T, X> visitor) throws X {
        return visitor.visit(this);
    }

    @Override
    public <X extends Throwable> void accept(VoidSweDataComponentVisitor<X> visitor) throws X {
        visitor.visit(this);
    }

    @Override
    public SweDataArray copy() {
        SweDataArray clone = new SweDataArray();
        copyValueTo(clone);
        if (isSetElementTyp()) {
            clone.setElementType(getElementType().copy());
        }
        if (isSetElementCount()) {
            clone.setElementCount(getElementCount().copy());
        }
        if (isSetEncoding()) {
            clone.setEncoding(getEncoding().copy());
        }
        if (isSetValues()) {
            clone.setValues(Lists.newArrayList(getValues()));
        }
        return clone;
    }
}
