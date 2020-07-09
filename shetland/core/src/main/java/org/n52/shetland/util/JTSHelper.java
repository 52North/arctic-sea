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

import java.util.Arrays;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for the Java Topology Suite.
 *
 * @since 1.0.0
 *
 */
public class JTSHelper {

    public static final String WKT_POLYGON = "Polygon";

    public static final String WKT_POINT = "Point";

    public static final CoordinateFilter COORDINATE_SWITCHING_FILTER = coord -> {
        double tmp = coord.x;
        coord.x = coord.y;
        coord.y = tmp;
    };

    private static final Logger LOGGER = LoggerFactory.getLogger(JTSHelper.class);

    protected JTSHelper() {
    }

    /**
     * Creates a JTS Geometry from an WKT representation. Switches the
     * coordinate order if needed.
     *
     * @param wkt
     *            WKT representation of the geometry
     * @param srid
     *            the SRID of the newly created geometry
     *
     * @return JTS Geometry object
     *
     * @throws ParseException
     *             If an error occurs
     */
    public static Geometry createGeometryFromWKT(String wkt, int srid) throws ParseException {
        WKTReader wktReader = getWKTReaderForSRID(srid);
        LOGGER.debug("FOI Geometry: {}", wkt);
        return wktReader.read(wkt);
    }

    public static WKTReader getWKTReaderForSRID(int srid) {
        if (srid <= 0) {
            return new WKTReader(new GeometryFactory());
        }
        return new WKTReader(getGeometryFactoryForSRID(srid));
    }

    /**
     * Get the coordinates of a Geometry as String.
     *
     * @param geom
     *            Geometry to get coordinates
     * @return Coordinates as String
     */
    public static String getCoordinatesString(Geometry geom) {
        return getCoordinatesString(geom.getCoordinates());
    }

    public static String getCoordinatesString(Coordinate[] sourceCoords) {
        StringBuilder builder = new StringBuilder();
        if (sourceCoords.length > 0) {
            getCoordinateString(builder, sourceCoords[0]);
            for (int i = 1; i < sourceCoords.length; ++i) {
                getCoordinateString(builder.append(' '), sourceCoords[i]);
            }
        }
        return builder.toString();
    }

    protected static StringBuilder getCoordinateString(StringBuilder builder, Coordinate coordinate) {
        builder.append(coordinate.getX());
        builder.append(' ');
        builder.append(coordinate.getY());
        if (!Double.isNaN(coordinate.getZ())) {
            builder.append(' ');
            builder.append(coordinate.getZ());
        }
        return builder;
    }

    /**
     * Creates a WKT Polygon representation from lower and upper corner values.
     *
     * @param lowerCorner
     *            Lower corner coordinates
     * @param upperCorner
     *            Upper corner coordinates
     * @return WKT Polygon
     */
    public static String createWKTPolygonFromEnvelope(String lowerCorner, String upperCorner) {
        final String[] splittedLowerCorner = lowerCorner.split(" ");
        final String[] splittedUpperCorner = upperCorner.split(" ");
        String minx = splittedLowerCorner[0];
        String miny = splittedLowerCorner[1];
        String maxx = splittedUpperCorner[0];
        String maxy = splittedUpperCorner[1];

        return createWKTPolygonFromEnvelope(minx, miny, maxx, maxy);
    }

    private static String createWKTPolygonFromEnvelope(String minx, String miny, String maxx, String maxy) {
        StringBuilder sb = new StringBuilder();
        sb.append(WKT_POLYGON).append(" ((");
        sb.append(minx).append(' ').append(miny).append(',');
        sb.append(minx).append(' ').append(maxy).append(',');
        sb.append(maxx).append(' ').append(maxy).append(',');
        sb.append(maxx).append(' ').append(miny).append(',');
        sb.append(minx).append(' ').append(miny).append("))");
        return sb.toString();
    }

    public static Envelope createEnvelopeFromLowerUpperCorner(String lowerCorner, String upperCorner) {
        final String[] splittedLowerCorner = lowerCorner.split(" ");
        final String[] splittedUpperCorner = upperCorner.split(" ");
        double minx = Double.parseDouble(splittedLowerCorner[0]);
        double miny = Double.parseDouble(splittedLowerCorner[1]);
        double maxx = Double.parseDouble(splittedUpperCorner[0]);
        double maxy = Double.parseDouble(splittedUpperCorner[1]);
        return new Envelope(minx, maxx, miny, maxy);
    }

    public static Geometry createPolygonFromEnvelope(double[] envelope, int srid) {
        if (envelope.length != 4) {
            throw new IllegalArgumentException();
        }
        return createPolygonFromEnvelope(envelope[0], envelope[1], envelope[2], envelope[3], srid);
    }

    public static Geometry createPolygonFromEnvelope(double minx, double miny, double maxx, double maxy, int srid) {
        GeometryFactory fac = getGeometryFactoryForSRID(srid);
        return fac.createPolygon(new Coordinate[] { new Coordinate(minx, miny), new Coordinate(minx, maxy),
            new Coordinate(maxx, maxy), new Coordinate(maxx, miny), new Coordinate(minx, miny) });
    }

    /**
     * Switches the coordinates of a JTS Geometry.
     *
     * @param <G>
     *            the geometry type
     * @param geometry
     *            Geometry to switch coordinates.
     * @return Geometry with switched coordinates
     */
    public static <G extends Geometry> G switchCoordinateAxisOrder(G geometry) {
        if (geometry == null) {
            return null;
        }
        @SuppressWarnings("unchecked")
        // if (geometry instanceof LinearRing) {
        // getSwitchCoordinateGeometryFactoryForSRID(geometry.getSRID()).createLinearRing(geometry.getCoordinates())
        // }
        G geom = (G) getSwitchCoordinateGeometryFactoryForSRID(geometry.getSRID()).convertSequence(geometry);
        geom.apply(COORDINATE_SWITCHING_FILTER);
        geom.geometryChanged();
        return geom;
    }

    public static GeometryFactory getGeometryFactory(Geometry geometry) {
        if (geometry.getFactory().getSRID() > 0 || geometry.getSRID() == 0) {
            return geometry.getFactory();
        } else {
            return getGeometryFactoryForSRID(geometry.getSRID());
        }
    }

    public static GeometryFactory getGeometryFactoryForSRID(int srid) {
        return new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
    }

    public static SwitchCoordinateGeometryFactory getSwitchCoordinateGeometryFactoryForSRID(int srid) {
        return new SwitchCoordinateGeometryFactory(new PrecisionModel(PrecisionModel.FLOATING), srid);
    }

    /**
     * Creates a WKT Point string form coordinate string.
     *
     * @param coordinates
     *            Coordinate string
     * @return WKT Point string
     */
    public static String createWKTPointFromCoordinateString(String coordinates) {
        return WKT_POINT + "(" + coordinates + ")";
    }

    public static boolean isNotEmpty(Geometry geometry) {
        return geometry != null && !geometry.isEmpty();
    }

    /**
     * Fix for Binary-Incompatible-Change in JTS 1.17.0 Polygon getExteriorRing() which
     * returns LinearRing instead of a LineString. This changes occurs
     * errors in the SOS with Hinernate/Geolatte until the 3rd party
     * libraries have not updated to JTS 1.17.0.
     *
     * @param polygon
     *            polygon to get exterior ring from
     * @return the coordinates
     */
    public static Coordinate[] getExteriorRingCoordinatesFromPolygon(Polygon polygon) {
        int interiorNumPoints = 0;
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            interiorNumPoints += polygon.getInteriorRingN(i)
                    .getNumPoints();
        }
        return interiorNumPoints == 0 ? polygon.getCoordinates()
                : Arrays.copyOfRange(polygon.getCoordinates(), 0, polygon.getCoordinates().length - interiorNumPoints);
    }

    private static class SwitchCoordinateGeometryFactory extends GeometryFactory {

        private static final long serialVersionUID = -4397568293678841518L;

        SwitchCoordinateGeometryFactory(PrecisionModel precisionModel, int srid) {
            super(precisionModel, srid);
        }

        public Geometry convertSequence(Geometry geometry) {
            if (geometry instanceof Point) {
                return createPoint(((Point) geometry).getCoordinateSequence());
            } else if (geometry instanceof LinearRing) {
                return createLinearRing(((LinearRing) geometry).getCoordinates());
            } else if (geometry instanceof LineString) {
                return createLineString(((LineString) geometry).getCoordinates());
            } else if (geometry instanceof Polygon) {
                LinearRing[] linearRings = new LinearRing[((Polygon) geometry).getNumInteriorRing()];
                for (int i = 0; i < ((Polygon) geometry).getNumInteriorRing(); i++) {
                    linearRings[i] = (LinearRing) convertSequence(((Polygon) geometry).getInteriorRingN(i));
                }
                return createPolygon((LinearRing) convertSequence(getExteriorRingFromPolygon((Polygon) geometry)),
                        linearRings);
            } else if (geometry instanceof MultiPoint) {
                return createMultiPointFromCoords(((MultiPoint) geometry).getCoordinates());
            } else if (geometry instanceof MultiLineString) {
                LineString[] lineStrings = new LineString[((MultiLineString) geometry).getNumGeometries()];
                for (int i = 0; i < ((MultiLineString) geometry).getNumGeometries(); i++) {
                    lineStrings[i] = (LineString) convertSequence(((MultiLineString) geometry).getGeometryN(i));
                }
                return createMultiLineString(lineStrings);
            } else if (geometry instanceof MultiPolygon) {
                Polygon[] polygons = new Polygon[((MultiPolygon) geometry).getNumGeometries()];
                for (int i = 0; i < ((MultiPolygon) geometry).getNumGeometries(); i++) {
                    polygons[i] = (Polygon) convertSequence(((MultiPolygon) geometry).getGeometryN(i));
                }
                return createMultiPolygon(polygons);
            } else if (geometry instanceof GeometryCollection) {
                Geometry[] geometries = new Geometry[((GeometryCollection) geometry).getNumGeometries()];
                for (int i = 0; i < ((GeometryCollection) geometry).getNumGeometries(); i++) {
                    geometries[i] = convertSequence(((GeometryCollection) geometry).getGeometryN(i));
                }
                return createGeometryCollection(geometries);
            }
            return geometry;
        }


        /**
         * Fix for Binary-Incompatible-Change in JTS 1.17.0 Polygon getExteriorRing() which
         * returns LinearRing instead of a LineString. This changes occurs
         * errors in the SOS with Hinernate/Geolatte until the 3rd party
         * libraries have not updated to JTS 1.17.0.
         *
         * @param polygon
         *            polygon to get exterior ring from
         * @return the linear ring
         */
        private LinearRing getExteriorRingFromPolygon(Polygon polygon) {
            return createLinearRing(getExteriorRingCoordinatesFromPolygon(polygon));
        }

        @Override
        public Point createPoint(CoordinateSequence coordinates) {
            return super.createPoint(convert(coordinates));
        }

        @Override
        public LineString createLineString(CoordinateSequence coordinates) {
            return super.createLineString(convert(coordinates));
        }

        @Override
        public Polygon createPolygon(CoordinateSequence coordinates) {
            return super.createPolygon(convert(coordinates));
        }

        @Override
        public LinearRing createLinearRing(CoordinateSequence coordinates) {
            return super.createLinearRing(convert(coordinates));
        }

        @Override
        public MultiPoint createMultiPoint(CoordinateSequence coordinates) {
            return super.createMultiPoint(convert(coordinates));
        }

        private CoordinateSequence convert(CoordinateSequence coordinates) {
            return new CoordinateArraySequence(coordinates);
        }

    }

}
