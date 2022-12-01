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

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.shetland.ogc.swe.SweConstants.SweCoordinateNames;
import org.n52.shetland.ogc.swe.SweConstants.SweDataComponentType;
import org.n52.shetland.ogc.swe.simpleType.SweQuantity;
import org.n52.shetland.ogc.swe.simpleType.SweTimeRange;
import org.n52.shetland.util.CRSHelper;
import org.n52.shetland.util.ReferencedEnvelope;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

/**
 * @since 1.0.0
 *
 */
public class SweEnvelope
        extends SweAbstractDataComponent {
    private String referenceFrame;
    private SweVector upperCorner;
    private SweVector lowerCorner;
    private SweTimeRange time;
    private final boolean northingFirst;

    public SweEnvelope(boolean northingFirst) {
        this(null, null, null, null, northingFirst);
    }

    public SweEnvelope(String referenceFrame, SweVector upperCorner, SweVector lowerCorner, boolean northingFirst) {
        this(referenceFrame, upperCorner, lowerCorner, null, northingFirst);
    }

    public SweEnvelope(ReferencedEnvelope sosEnvelope, String uom, boolean northingFirst) {
        this(String.valueOf(sosEnvelope.getSrid()), createUpperCorner(sosEnvelope, uom, northingFirst),
                createLowerCorner(sosEnvelope, uom, northingFirst), northingFirst);
    }

    public SweEnvelope(
            String referenceFrame, SweVector upperCorner, SweVector lowerCorner, SweTimeRange time,
            boolean northingFirst) {
        this.referenceFrame = referenceFrame;
        this.upperCorner = upperCorner;
        this.lowerCorner = lowerCorner;
        this.time = time;
        this.northingFirst = northingFirst;
    }

    public String getReferenceFrame() {
        return referenceFrame;
    }

    public boolean isReferenceFrameSet() {
        return getReferenceFrame() != null;
    }

    public SweEnvelope setReferenceFrame(String referenceFrame) {
        this.referenceFrame = referenceFrame;
        return this;
    }

    public SweVector getUpperCorner() {
        return upperCorner;
    }

    public boolean isUpperCornerSet() {
        return getUpperCorner() != null;
    }

    public SweEnvelope setUpperCorner(SweVector upperCorner) {
        this.upperCorner = upperCorner;
        return this;
    }

    public SweVector getLowerCorner() {
        return lowerCorner;
    }

    public boolean isLowerCornerSet() {
        return getLowerCorner() != null;
    }

    public SweEnvelope setLowerCorner(SweVector lowerCorner) {
        this.lowerCorner = lowerCorner;
        return this;
    }

    public SweTimeRange getTime() {
        return time;
    }

    public boolean isTimeSet() {
        return getTime() != null;
    }

    public SweEnvelope setTime(SweTimeRange time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass()).add("upperCorner", getUpperCorner())
                .add("lowerCorner", getLowerCorner()).add("time", getTime()).add("referenceFrame", getReferenceFrame())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getReferenceFrame(), getUpperCorner(), getLowerCorner(), getTime());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SweEnvelope) {
            SweEnvelope other = (SweEnvelope) obj;
            return Objects.equal(getReferenceFrame(), other.getReferenceFrame())
                    && Objects.equal(getUpperCorner(), other.getUpperCorner())
                    && Objects.equal(getLowerCorner(), other.getLowerCorner())
                    && Objects.equal(getTime(), other.getTime());

        }
        return false;
    }

    @Override
    public SweDataComponentType getDataComponentType() {
        return SweDataComponentType.Envelope;
    }

    public ReferencedEnvelope toReferencedEnvelope() throws OwsExceptionReport {
        int srid = CRSHelper.parseSrsName(getReferenceFrame());
        return new ReferencedEnvelope(toEnvelope(), srid);
    }

    public Envelope toEnvelope() throws OwsExceptionReport {
        Coordinate min = getLowerCornerAsCoordinate();
        Coordinate max = getUpperCornerAsCoordinate();
        if (min != null && max != null) {
            if (this.northingFirst) {
                return new Envelope(min.y, max.y, min.x, max.x);
            } else {
                return new Envelope(min.x, max.x, min.y, max.y);
            }
        }
        return null;
    }

    public Coordinate getLowerCornerAsCoordinate() {
        if (isLowerCornerSet()) {
            return getSweVectorAsCoordinate(getLowerCorner());
        }
        return null;
    }

    public Coordinate getUpperCornerAsCoordinate() {
        if (isUpperCornerSet()) {
            return getSweVectorAsCoordinate(getUpperCorner());
        }
        return null;
    }

    private Coordinate getSweVectorAsCoordinate(SweVector vector) {
        if (vector != null && vector.isSetCoordinates() && vector.getCoordinates().size() >= 2) {
            Double x = extractDouble(vector.getCoordinates().get(0));
            Double y = extractDouble(vector.getCoordinates().get(1));
            if (x != null && y != null) {
                return new Coordinate(x, y);
            }
        }
        return null;
    }

    private Double extractDouble(SweCoordinate<?> coord) {
        if (coord != null && coord.getValue() != null && coord.getValue().getValue() instanceof Number) {
            return coord.getValue().getValue().doubleValue();
        }
        return null;
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
    public SweEnvelope copy() {
        SweEnvelope clone = new SweEnvelope(this.northingFirst);
        copyValueTo(clone);
        clone.setReferenceFrame(getReferenceFrame());
        if (isLowerCornerSet()) {
            clone.setLowerCorner(getLowerCorner().copy());
        }
        if (isUpperCornerSet()) {
            clone.setUpperCorner(getUpperCorner().copy());
        }
        return clone;
    }

    private static SweVector createLowerCorner(ReferencedEnvelope env, String uom, boolean northingFirst) {
        if (northingFirst) {
            return createSweVector(env.getEnvelope().getMinY(), env.getEnvelope().getMinX(), uom);
        } else {
            return createSweVector(env.getEnvelope().getMinX(), env.getEnvelope().getMinY(), uom);
        }
    }

    private static SweVector createUpperCorner(ReferencedEnvelope env, String uom, boolean northingFirst) {
        if (northingFirst) {
            return createSweVector(env.getEnvelope().getMaxY(), env.getEnvelope().getMaxX(), uom);
        } else {
            return createSweVector(env.getEnvelope().getMaxX(), env.getEnvelope().getMaxY(), uom);
        }
    }

    private static SweVector createSweVector(double x, double y, String uom) {
        SweQuantity xCoord = new SweQuantity().setAxisID(SweConstants.X_AXIS).setValue(x).setUom(uom);
        SweQuantity yCoord = new SweQuantity().setAxisID(SweConstants.Y_AXIS).setValue(y).setUom(uom);
        return new SweVector(new SweCoordinate<>(SweCoordinateNames.EASTING, xCoord),
                new SweCoordinate<>(SweCoordinateNames.NORTHING, yCoord));
    }
}
