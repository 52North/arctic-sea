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
package org.n52.shetland.ogc.sensorML.elements;

import java.util.List;

import org.n52.iceland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.StringHelper;
import org.n52.shetland.ogc.swe.SweAbstractDataComponent;
import org.n52.shetland.ogc.swe.SweCoordinate;
import org.n52.shetland.ogc.swe.SweDataComponentVisitor;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.SweVector;
import org.n52.shetland.ogc.swe.VoidSweDataComponentVisitor;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;

import com.google.common.collect.Lists;

/**
 * SOS internal representation of SensorML position
 *
 * @since 4.0.0
 */
public class SmlPosition extends SweAbstractDataComponent {

    private boolean fixed;

    private String referenceFrame;

    private List<? extends SweCoordinate<?>> position;

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
     *                       Position name
     * @param fixed
     *                       is fixed
     * @param referenceFrame
     *                       Position reference frame
     * @param position
     *                       Position coordinates
     */
    public SmlPosition(final String name, final boolean fixed,
                       final String referenceFrame,
                       final List<SweCoordinate<?>> position) {
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
     *                       Position name
     * @param fixed
     *                       is fixed
     * @param referenceFrame
     *                       Position reference frame
     * @param position
     *                       Position coordinates
     */
    public SmlPosition(final CodeType name, final boolean fixed,
                       final String referenceFrame,
                       final List<SweCoordinate<?>> position) {
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
     *              the fixed to set
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
     *                       the referenceFrame to set
     *
     * @return This object
     */
    public SmlPosition setReferenceFrame(final String referenceFrame) {
        this.referenceFrame = referenceFrame;
        return this;
    }

    public boolean isSetReferenceFrame() {
        return StringHelper.isNotEmpty(referenceFrame);
    }

    /**
     * @return the position
     */
    public List<? extends SweCoordinate<?>> getPosition() {
        if (!isSetPosition()) {
            if (isSetVector() && getVector().isSetCoordinates()) {
                if (!isSetName() && vector.isSetName()) {
                    setName(vector.getName());
                }
                return vector.getCoordinates();
            } else if (isSetAbstractDataComponent() && getAbstractDataComponent() instanceof SweDataRecord) {
                SweDataRecord dataRecord = (SweDataRecord) getAbstractDataComponent();
                if (dataRecord.isSetFields()) {
                    List<SweCoordinate<?>> coordinates = Lists.newArrayList();
                    for (SweField field : dataRecord.getFields()) {
                        if (field.getElement() instanceof SweQuantity) {
                            coordinates.add(new SweCoordinate<Double>(field.getName().getValue(), (SweQuantity)field.getElement()));
                        }
                    }
                    return coordinates;
                }
            }
        }
        return position;
    }

    /**
     * @param position
     *                 the position to set
     *
     * @return This object
     */
    public SmlPosition setPosition(List<? extends SweCoordinate<?>> position) {
        this.position = position;
        return this;
    }

    public boolean isSetPosition() {
        return CollectionHelper.isNotEmpty(position);
    }

    public SweVector getVector() {
        if (!isSetVector() && isSetPosition()) {
            SweVector vector = (SweVector) copyValueTo(new SweVector(getPosition()));
            vector.setReferenceFrame(getReferenceFrame());
            if (isSetName()) {
                vector.setName(getName());
            }
            return vector;
        }
        return vector;
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
            setVector((SweVector)dataComponent);
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
    public <T> T accept(SweDataComponentVisitor<T> visitor)
            throws OwsExceptionReport {
        return visitor.visit(this);
    }

    @Override
    public void accept(VoidSweDataComponentVisitor visitor)
            throws OwsExceptionReport {
        visitor.visit(this);
    }

    @Override
    public SmlPosition clone() throws CloneNotSupportedException {
        SmlPosition clone = new SmlPosition();
        copyValueTo(clone);
        if (isSetPosition()) {
            clone.setPosition(getPosition());
        }
        if (isSetVector()) {
            clone.setVector(getVector().clone());
        } else if (isSetAbstractDataComponent()) {
            clone.setAbstractDataComponent(getAbstractDataComponent().clone());
        }
        copyValueTo(clone);
        return clone;
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
