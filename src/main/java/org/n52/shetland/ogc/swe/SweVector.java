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
package org.n52.shetland.ogc.swe;

import java.util.Arrays;
import java.util.List;

import org.n52.iceland.ogc.swe.SweConstants.SweDataComponentType;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * @since 4.0.0
 *
 */
public class SweVector extends SweAbstractDataComponent {
    private List<? extends SweCoordinate<? extends Number>> coordinates;

    private String referenceFrame;

    private String localFrame;

    public SweVector(List<? extends SweCoordinate<? extends Number>> coordinates) {
        this.coordinates = coordinates;
    }

    @SafeVarargs
    @SuppressWarnings("varargs")
    public SweVector(SweCoordinate<? extends Number>... coordinates) {
        this(Arrays.asList(coordinates));
    }

    public SweVector() {
        this((List<SweCoordinate<? extends Number>>) null);
    }

    public List<? extends SweCoordinate<? extends Number>> getCoordinates() {
        return coordinates;
    }

    public SweVector setCoordinates(final List<? extends SweCoordinate<? extends Number>> coordinates) {
        this.coordinates = coordinates;
        return this;
    }

    public boolean isSetCoordinates() {
        return getCoordinates() != null && !getCoordinates().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s[coordinates=%s]", getClass().getSimpleName(), getCoordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.coordinates);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SweVector other = (SweVector) obj;
        if (!Objects.equal(coordinates, other.coordinates)) {
            return false;
        }
        return true;
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.Vector;
    }

    public void setReferenceFrame(String referenceFrame) {
        this.referenceFrame = referenceFrame;

    }

    public String getReferenceFrame() {
        return referenceFrame;
    }

    public boolean isSetReferenceFrame() {
        return this.referenceFrame != null && !this.referenceFrame.isEmpty();
    }

    public void setLocalFrame(String localFrame) {
        this.localFrame = localFrame;

    }

    public String getLocalFrame() {
        return localFrame;
    }

    public boolean isSetLocalFrame() {
        return this.localFrame != null && !this.localFrame.isEmpty();
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
    public SweVector clone() throws CloneNotSupportedException {
        SweVector clone = new SweVector();
        copyValueTo(clone);
        if (isSetCoordinates()) {
            List<SweCoordinate<?>> clonedList = Lists.newArrayListWithCapacity(getCoordinates().size());
            for (SweCoordinate<?> sweCoordinate : getCoordinates()) {
                clonedList.add(sweCoordinate.clone());
            }
            clone.setCoordinates(coordinates);
        }
        return clone;
    }

    @Override
    public SweAbstractDataComponent copyValueTo(SweAbstractDataComponent copy) {
        super.copyValueTo(copy);
        if (copy instanceof SweVector) {
            ((SweVector) copy).setReferenceFrame(getReferenceFrame());
            ((SweVector) copy).setLocalFrame(getLocalFrame());
            ((SweVector) copy).setCoordinates(getCoordinates());
        }
        return copy;
    }
}
