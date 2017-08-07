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
package org.n52.shetland.ogc.sensorML.elements;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.SweCoordinate;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweVector;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.util.CollectionHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * SOS internal representation of SensorML position
 *
 * @since 1.0.0
 */
public class SmlPosition
        extends SweAbstractDataComponent {

    private boolean fixed;

    private String referenceFrame;

    private List<? extends SweCoordinate<? extends Number>> position;

    private SweVector vector;

    private SweAbstractDataComponent dataComponent;

    /**
     * default constructor
     */
    public SmlPosition() {
        super();
    }

    /**
     * constructor
     *
     * @param name
     *            Position name
     * @param fixed
     *            is fixed
     * @param referenceFrame
     *            Position reference frame
     * @param position
     *            Position coordinates
     */
    public SmlPosition(
            final String name, final boolean fixed, final String referenceFrame,
            final List<SweCoordinate<? extends Number>> position) {
        super();
        setName(name);
        this.fixed = fixed;
        this.referenceFrame = referenceFrame;
        this.position = position;
    }

    /**
     * constructor
     *
     * @param name
     *            Position name
     * @param fixed
     *            is fixed
     * @param referenceFrame
     *            Position reference frame
     * @param position
     *            Position coordinates
     */
    public SmlPosition(
            final CodeType name, final boolean fixed, final String referenceFrame,
            final List<SweCoordinate<? extends Number>> position) {
        super();
        setName(name);
        this.fixed = fixed;
        this.referenceFrame = referenceFrame;
        this.position = position;
    }

    /**
     * @return the fixed
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * @param fixed
     *            the fixed to set
     *
     * @return This object
     */
    public SmlPosition setFixed(final boolean fixed) {
        this.fixed = fixed;
        return this;
    }

    /**
     * @return the referenceFrame
     */
    public String getReferenceFrame() {
        if (!isSetReferenceFrame() && isSetVector() && getVector().isSetReferenceFrame()) {
            return getVector().getReferenceFrame();
        }
        return referenceFrame;
    }

    /**
     * @param referenceFrame
     *            the referenceFrame to set
     *
     * @return This object
     */
    public SmlPosition setReferenceFrame(final String referenceFrame) {
        this.referenceFrame = referenceFrame;
        return this;
    }

    public boolean isSetReferenceFrame() {
        return !Strings.isNullOrEmpty(referenceFrame);
    }

    /**
     * @return the position
     */
    public List<? extends SweCoordinate<? extends Number>> getPosition() {
        if (!isSetPosition()) {
            if (isSetVector() && getVector().isSetCoordinates()) {
                if (!isSetName() && vector.isSetName()) {
                    setName(vector.getName());
                }
                return vector.getCoordinates();
            } else if (isSetAbstractDataComponent() && getAbstractDataComponent() instanceof SweDataRecord) {
                SweDataRecord dataRecord = (SweDataRecord) getAbstractDataComponent();
                if (dataRecord.isSetFields()) {
                    return dataRecord.getFields().stream().map(field -> {
                        if (field.getElement() instanceof SweQuantity) {
                            return new SweCoordinate<>(field.getName().getValue(), (SweQuantity) field.getElement());
                        } else if (field.getElement() instanceof SweCount) {
                            return new SweCoordinate<>(field.getName().getValue(), (SweCount) field.getElement());
                        }
                        return null;
                    }).collect(toList());
                }
            }
        }
        return position;
    }

    /**
     * @param position
     *            the position to set
     *
     * @return This object
     */
    public SmlPosition setPosition(List<? extends SweCoordinate<? extends Number>> position) {
        this.position = position;
        return this;
    }

    public boolean isSetPosition() {
        return CollectionHelper.isNotEmpty(position);
    }

    public SweVector getVector() {
        if (!isSetVector() && isSetPosition()) {
            SweVector v = (SweVector) copyValueTo(new SweVector(getPosition()));
            v.setReferenceFrame(getReferenceFrame());
            if (isSetName()) {
                v.setName(getName());
            }
            return v;
        }
        return this.vector;
    }

    public void setVector(SweVector vector) {
        this.vector = vector;
    }

    public boolean isSetVector() {
        return vector != null;
    }

    public SweAbstractDataComponent getAbstractDataComponent() {
        if (!isSetAbstractDataComponent() && isSetVector()) {
            return vector;
        }
        return dataComponent;
    }

    public void setAbstractDataComponent(SweAbstractDataComponent dataComponent) {
        if (dataComponent instanceof SweVector) {
            setVector((SweVector) dataComponent);
        }
        this.dataComponent = dataComponent;
    }

    public boolean isSetAbstractDataComponent() {
        return dataComponent != null || isSetVector();
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.Position;
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
    public SmlPosition copy() {
        SmlPosition copy = new SmlPosition();
        copyValueTo(copy);
        if (isSetPosition()) {
            copy.setPosition(getPosition());
        }
        if (isSetVector()) {
            copy.setVector(getVector().copy());
        } else if (isSetAbstractDataComponent()) {
            copy.setAbstractDataComponent(getAbstractDataComponent().copy());
        }
        copyValueTo(copy);
        return copy;
    }

    @Override
    public SweAbstractDataComponent copyValueTo(SweAbstractDataComponent copy) {
        super.copyValueTo(copy);
        if (copy instanceof SmlPosition) {
            ((SmlPosition) copy).setFixed(isFixed());
            ((SmlPosition) copy).setReferenceFrame(getReferenceFrame());
            if (isSetPosition()) {
                ((SmlPosition) copy).setPosition(Lists.newArrayList(getPosition()));
            }
            if (isSetVector()) {
                ((SmlPosition) copy).setVector(getVector());
            } else if (isSetAbstractDataComponent()) {
                ((SmlPosition) copy).setAbstractDataComponent(getAbstractDataComponent());
            }
        }
        return copy;
    }
}
