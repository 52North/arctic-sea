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
package org.n52.svalbard.encode.json;

import org.n52.svalbard.coding.json.JSONConstants;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 *
 * @since 1.0.0
 */
public class GeoJSONEncoder
        extends JSONEncoder<Geometry> {
    public static final int DEFAULT_SRID = 4326;

    public static final String SRID_LINK_PREFIX = "http://www.opengis.net/def/crs/EPSG/0/";

    private final JsonNodeFactory jsonFactory = JsonNodeFactory.withExactBigDecimals(false);

    public GeoJSONEncoder() {
        super(Geometry.class);
    }

    @Override
    public ObjectNode encodeJSON(Geometry value)
            throws JSONEncodingException {
        if (value == null) {
            return null;
        } else {
            return encodeGeometry(value, DEFAULT_SRID);
        }
    }

    protected ObjectNode encodeGeometry(Geometry geometry, int parentSrid)
            throws JSONEncodingException {
        Preconditions.checkNotNull(geometry);
        if (geometry.isEmpty()) {
            return null;
        } else if (geometry instanceof Point) {
            return encode((Point) geometry, parentSrid);
        } else if (geometry instanceof LineString) {
            return encode((LineString) geometry, parentSrid);
        } else if (geometry instanceof Polygon) {
            return encode((Polygon) geometry, parentSrid);
        } else if (geometry instanceof MultiPoint) {
            return encode((MultiPoint) geometry, parentSrid);
        } else if (geometry instanceof MultiLineString) {
            return encode((MultiLineString) geometry, parentSrid);
        } else if (geometry instanceof MultiPolygon) {
            return encode((MultiPolygon) geometry, parentSrid);
        } else if (geometry instanceof GeometryCollection) {
            return encode((GeometryCollection) geometry, parentSrid);
        } else {
            throw new JSONEncodingException("unknown geometry type " + geometry.getGeometryType());
        }
    }

    protected ObjectNode encode(Point geometry, int parentSrid) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = jsonFactory.objectNode();
        json.put(JSONConstants.TYPE, JSONConstants.POINT);
        json.set(JSONConstants.COORDINATES, encodeCoordinates(geometry));
        encodeCRS(json, geometry, parentSrid);
        return json;
    }

    protected ObjectNode encode(LineString geometry, int parentSrid) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = jsonFactory.objectNode();
        json.put(JSONConstants.TYPE, JSONConstants.LINE_STRING).set(JSONConstants.COORDINATES,
                encodeCoordinates(geometry));
        encodeCRS(json, geometry, parentSrid);
        return json;
    }

    protected ObjectNode encode(Polygon geometry, int parentSrid) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = jsonFactory.objectNode();
        json.put(JSONConstants.TYPE, JSONConstants.POLYGON).set(JSONConstants.COORDINATES,
                encodeCoordinates(geometry));
        encodeCRS(json, geometry, parentSrid);
        return json;
    }

    protected ObjectNode encode(MultiPoint geometry, int parentSrid) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = jsonFactory.objectNode();
        ArrayNode list = json.put(JSONConstants.TYPE, JSONConstants.MULTI_POINT).putArray(JSONConstants.COORDINATES);
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Point) geometry.getGeometryN(i)));
        }
        encodeCRS(json, geometry, parentSrid);
        return json;
    }

    protected ObjectNode encode(MultiLineString geometry, int parentSrid) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = jsonFactory.objectNode();
        ArrayNode list =
                json.put(JSONConstants.TYPE, JSONConstants.MULTI_LINE_STRING).putArray(JSONConstants.COORDINATES);
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((LineString) geometry.getGeometryN(i)));
        }
        encodeCRS(json, geometry, parentSrid);
        return json;
    }

    protected ObjectNode encode(MultiPolygon geometry, int parentSrid) {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = jsonFactory.objectNode();
        ArrayNode list = json.put(JSONConstants.TYPE, JSONConstants.MULTI_POLYGON).putArray(JSONConstants.COORDINATES);
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            list.add(encodeCoordinates((Polygon) geometry.getGeometryN(i)));
        }
        encodeCRS(json, geometry, parentSrid);
        return json;
    }

    public ObjectNode encode(GeometryCollection geometry, int parentSrid)
            throws JSONEncodingException {
        Preconditions.checkNotNull(geometry);
        ObjectNode json = jsonFactory.objectNode();
        ArrayNode geometries =
                json.put(JSONConstants.TYPE, JSONConstants.GEOMETRY_COLLECTION).putArray(JSONConstants.GEOMETRIES);
        int srid = encodeCRS(json, geometry, parentSrid);
        for (int i = 0; i < geometry.getNumGeometries(); ++i) {
            geometries.add(encodeGeometry(geometry.getGeometryN(i), srid));
        }
        return json;
    }

    protected ArrayNode encodeCoordinate(Coordinate coordinate) {

        ArrayNode array = jsonFactory.arrayNode().add(coordinate.x).add(coordinate.y);

        if (!Double.isNaN(coordinate.z)) {
            array.add(coordinate.z);
        }

        return array;
    }

    protected ArrayNode encodeCoordinates(CoordinateSequence coordinates) {
        ArrayNode list = jsonFactory.arrayNode();
        for (int i = 0; i < coordinates.size(); ++i) {
            list.add(encodeCoordinate(coordinates.getCoordinate(i)));
        }
        return list;
    }

    protected ArrayNode encodeCoordinates(Point geometry) {
        return encodeCoordinate(geometry.getCoordinate());
    }

    protected ArrayNode encodeCoordinates(LineString geometry) {
        return encodeCoordinates(geometry.getCoordinateSequence());
    }

    protected ArrayNode encodeCoordinates(Polygon geometry) {
        ArrayNode list = jsonFactory.arrayNode();
        list.add(encodeCoordinates(geometry.getExteriorRing()));
        for (int i = 0; i < geometry.getNumInteriorRing(); ++i) {
            list.add(encodeCoordinates(geometry.getInteriorRingN(i)));
        }
        return list;
    }

    protected int encodeCRS(ObjectNode json, Geometry geometry, int parentSrid) {
        return encodeCRS(geometry.getSRID(), parentSrid, json);
    }

    protected int encodeCRS(int srid, int parentSrid, ObjectNode json) {
        if (srid == parentSrid || srid == 0 || (parentSrid == DEFAULT_SRID && srid == DEFAULT_SRID)) {
            return parentSrid;
        } else {
            json.putObject(JSONConstants.CRS).put(JSONConstants.TYPE, JSONConstants.LINK)
                    .putObject(JSONConstants.PROPERTIES).put(JSONConstants.HREF, SRID_LINK_PREFIX + srid);
            return srid;
        }
    }
}
