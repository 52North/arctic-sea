/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.CoordinateSequenceComparator;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.GeometryFilter;
import org.locationtech.jts.geom.IntersectionMatrix;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

public class UnknownGeometry extends Geometry {
    private static final long serialVersionUID = -1032955252468856386L;

    private Geometry geom;

    UnknownGeometry(Geometry geom) {
        super(geom.getFactory());
        this.geom = geom;
    }

    @Override
    public String getGeometryType() {
        return geom.getGeometryType();
    }

    @Override
    public int getSRID() {
        return geom.getSRID();
    }

    @Override
    public void setSRID(int SRID) {
        geom.setSRID(SRID);
    }

    @Override
    public GeometryFactory getFactory() {
        return geom.getFactory();
    }

    @Override
    public Object getUserData() {
        return geom.getUserData();
    }

    @Override
    public int getNumGeometries() {
        return geom.getNumGeometries();
    }

    @Override
    public Geometry getGeometryN(int n) {
        return geom.getGeometryN(n);
    }

    @Override
    public void setUserData(Object userData) {
        geom.setUserData(userData);
    }

    @Override
    public PrecisionModel getPrecisionModel() {
        return geom.getPrecisionModel();
    }

    @Override
    public Coordinate getCoordinate() {
        return geom.getCoordinate();
    }

    @Override
    public Coordinate[] getCoordinates() {
        return geom.getCoordinates();
    }

    @Override
    public int getNumPoints() {
        return geom.getNumPoints();
    }

    @Override
    public boolean isSimple() {
        return geom.isSimple();
    }

    @Override
    public boolean isValid() {
        return geom.isValid();
    }

    @Override
    public boolean isEmpty() {
        return geom.isEmpty();
    }

    @Override
    public double distance(Geometry g) {
        return geom.distance(g);
    }

    @Override
    public boolean isWithinDistance(Geometry geom, double distance) {
        return this.geom.isWithinDistance(geom, distance);
    }

    @Override
    public boolean isRectangle() {
        return geom.isRectangle();
    }

    @Override
    public double getArea() {
        return geom.getArea();
    }

    @Override
    public double getLength() {
        return geom.getLength();
    }

    @Override
    public Point getCentroid() {
        return geom.getCentroid();
    }

    @Override
    public Point getInteriorPoint() {
        return geom.getInteriorPoint();
    }

    @Override
    public int getDimension() {
        return geom.getDimension();
    }

    @Override
    public Geometry getBoundary() {
        return geom.getBoundary();
    }

    @Override
    public int getBoundaryDimension() {
        return geom.getBoundaryDimension();
    }

    @Override
    public Geometry getEnvelope() {
        return geom.getEnvelope();
    }

    @Override
    public Envelope getEnvelopeInternal() {
        return geom.getEnvelopeInternal();
    }

    @Override
    public void geometryChanged() {
        geom.geometryChanged();
    }

    @Override
    public boolean disjoint(Geometry g) {
        return geom.disjoint(g);
    }

    @Override
    public boolean touches(Geometry g) {
        return geom.touches(g);
    }

    @Override
    public boolean intersects(Geometry g) {
        return geom.intersects(g);
    }

    @Override
    public boolean crosses(Geometry g) {
        return geom.crosses(g);
    }

    @Override
    public boolean within(Geometry g) {
        return geom.within(g);
    }

    @Override
    public boolean contains(Geometry g) {
        return geom.contains(g);
    }

    @Override
    public boolean overlaps(Geometry g) {
        return geom.overlaps(g);
    }

    @Override
    public boolean covers(Geometry g) {
        return geom.covers(g);
    }

    @Override
    public boolean coveredBy(Geometry g) {
        return geom.coveredBy(g);
    }

    @Override
    public boolean relate(Geometry g, String intersectionPattern) {
        return geom.relate(g, intersectionPattern);
    }

    @Override
    public IntersectionMatrix relate(Geometry g) {
        return geom.relate(g);
    }

    @Override
    public boolean equals(Geometry g) {
        return geom.equals(g);
    }

    @Override
    public boolean equalsTopo(Geometry g) {
        return geom.equalsTopo(g);
    }

    @Override
    public boolean equals(Object o) {
        return geom.equals(o);
    }

    @Override
    public int hashCode() {
        return geom.hashCode();
    }

    @Override
    public String toString() {
        return geom.toString();
    }

    @Override
    public String toText() {
        return geom.toText();
    }

    @Override
    public Geometry buffer(double distance) {
        return geom.buffer(distance);
    }

    @Override
    public Geometry buffer(double distance, int quadrantSegments) {
        return geom.buffer(distance, quadrantSegments);
    }

    @Override
    public Geometry buffer(double distance, int quadrantSegments, int endCapStyle) {
        return geom.buffer(distance, quadrantSegments, endCapStyle);
    }

    @Override
    public Geometry convexHull() {
        return geom.convexHull();
    }

    @Override
    public Geometry reverse() {
        return geom.reverse();
    }

    @Override
    public Geometry intersection(Geometry other) {
        return geom.intersection(other);
    }

    @Override
    public Geometry union(Geometry other) {
        return geom.union(other);
    }

    @Override
    public Geometry difference(Geometry other) {
        return geom.difference(other);
    }

    @Override
    public Geometry symDifference(Geometry other) {
        return geom.symDifference(other);
    }

    @Override
    public Geometry union() {
        return geom.union();
    }

    @Override
    public boolean equalsExact(Geometry other, double tolerance) {
        return geom.equalsExact(other, tolerance);
    }

    @Override
    public boolean equalsExact(Geometry other) {
        return geom.equalsExact(other);
    }

    @Override
    public boolean equalsNorm(Geometry g) {
        return geom.equalsNorm(g);
    }

    @Override
    public void apply(CoordinateFilter filter) {
        geom.apply(filter);
    }

    @Override
    public void apply(CoordinateSequenceFilter filter) {
        geom.apply(filter);
    }

    @Override
    public void apply(GeometryFilter filter) {
        geom.apply(filter);
    }

    @Override
    public void apply(GeometryComponentFilter filter) {
        geom.apply(filter);
    }

    @Override
    public Geometry copy() {
        return new UnknownGeometry(geom.copy());
    }

    @Override
    public void normalize() {
        geom.normalize();
    }

    @Override
    public Geometry norm() {
        return new UnknownGeometry(geom.norm());
    }

    @Override
    public int compareTo(Object o) {
        return geom.compareTo(o);
    }

    @Override
    public int compareTo(Object o, CoordinateSequenceComparator comp) {
        return geom.compareTo(o, comp);
    }

    @Override
    protected Envelope computeEnvelopeInternal() {
        return geom.getEnvelopeInternal();
    }

    @Override
    protected int compareToSameClass(Object o) {
        return geom.compareTo(o);
    }

    @Override
    protected int compareToSameClass(Object o, CoordinateSequenceComparator comp) {
        return geom.compareTo(o, comp);
    }

    @Override
    protected Geometry copyInternal() {
        return new UnknownGeometry(geom.copy());
    }

    @Override
    protected Geometry reverseInternal() {
        return null;
    }

    @Override
    protected int getTypeCode() {
        return 0;
    }
}
