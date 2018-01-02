/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.values;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.n52.shetland.ogc.UoM;
import org.n52.shetland.ogc.om.PointValuePair;
import org.n52.shetland.ogc.om.values.visitor.ValueVisitor;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.util.JavaHelper;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Class that represents a multi point coverage
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class MultiPointCoverage
        implements DiscreteCoverage<List<PointValuePair>> {

    private static final String GML_ID_PREFIX = "mpc_";
    private final String gmlId;

    /**
     * Mesurement values
     */
    private final List<PointValuePair> value = new ArrayList<PointValuePair>(0);

    /**
     * Unit of measure
     */
    private UoM unit;
    private String rangeParameters;

    public MultiPointCoverage(String gmlId) {
        if (Strings.isNullOrEmpty(gmlId)) {
            this.gmlId = GML_ID_PREFIX + JavaHelper.generateID(toString());
        } else if (!gmlId.startsWith(GML_ID_PREFIX)) {
            this.gmlId = GML_ID_PREFIX + gmlId;
        } else {
            this.gmlId = gmlId;
        }
    }

    @Override
    public String getGmlId() {
        return gmlId;
    }

    @Override
    public List<PointValuePair> getValue() {
        Collections.sort(value);
        return value;
    }

    public PointValueLists getPointValue() {
        return new PointValueLists(getValue());
    }

    @Override
    public MultiPointCoverage setValue(List<PointValuePair> value) {
        this.value.clear();
        this.value.addAll(value);
        return this;
    }

    /**
     * Add time value pair value
     *
     * @param value
     *            Time value pair value to add
     */
    public void addValue(PointValuePair value) {
        this.value.add(value);
    }

    /**
     * Add time value pair values
     *
     * @param values
     *            Time value pair values to add
     */
    public void addValues(List<PointValuePair> values) {
        this.value.addAll(values);
    }

    @Override
    public void setUnit(String unit) {
        this.unit = new UoM(unit);
    }

    @Override
    public MultiPointCoverage setUnit(UoM unit) {
        this.unit = unit;
        return this;
    }

    @Override
    public String getUnit() {
        if (isSetUnit()) {
            return unit.getUom();
        }
        return null;
    }

    @Override
    public UoM getUnitObject() {
        return this.unit;
    }

    @Override
    public boolean isSetValue() {
        return CollectionHelper.isNotEmpty(getValue());
    }

    /**
     * Get the extent of all {@link Point}s
     *
     * @return The extent as {@link Polygon}
     */
    public Polygon getExtent() {
        if (isSetValue()) {
            int srid = -1;
            List<Coordinate> coordinates = Lists.newLinkedList();
            for (PointValuePair pointValuePair : getValue()) {
                Point point = pointValuePair.getPoint();
                coordinates.add(point.getCoordinate());
                if (point.getSRID() != srid) {
                    srid = point.getSRID();
                }
            }
            GeometryFactory geometryFactory;
            if (srid > 0) {
                geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
            } else {
                geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING));
            }
            return geometryFactory.createPolygon(coordinates.toArray(new Coordinate[coordinates.size()]));
        }
        return null;
    }

    @Override
    public List<Value<?>> getRangeSet() {
        return getPointValue().getValues();
    }

    @Override
    public String getRangeParameters() {
        return rangeParameters;
    }

    @Override
    public void setRangeParameters(String rangeParameters) {
        this.rangeParameters = rangeParameters;
    }

    @Override
    public boolean isSetRangeParameters() {
        return !Strings.isNullOrEmpty(getRangeParameters());
    }

    @Override
    public <X, E extends Exception> X accept(ValueVisitor<X, E> visitor)
            throws E {
        return visitor.visit(this);
    }

    /**
     * Element that holds {@link Point}s and {@link Value}s
     *
     * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
     * @since 1.0.0
     *
     */
    public static class PointValueLists {
        private final List<Point> points;
        private final List<Value<?>> values;

        public PointValueLists(List<PointValuePair> pointValuePairs) {
            this.points = pointValuePairs.stream().map(PointValuePair::getPoint).collect(toList());
            this.values = pointValuePairs.stream().map(PointValuePair::getValue).collect(toList());
        }

        /**
         * @return the points
         */
        public List<Point> getPoints() {
            return Collections.unmodifiableList(points);
        }

        /**
         * @return the values
         */
        public List<Value<?>> getValues() {
            return Collections.unmodifiableList(values);
        }

    }

}
