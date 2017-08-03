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
package org.n52.svalbard.decode.json;

import org.n52.svalbard.coding.json.GeoJSONDecodingException;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.coding.json.JSONValidator;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.decode.exception.DecodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GeoJSONDecoder
        extends JSONDecoder<Geometry> {

    public static final int DIM_2D = 2;
    public static final int DIM_3D = 3;

    private static final String[] SRS_LINK_PREFIXES =
            { "http://www.opengis.net/def/crs/EPSG/0/", "http://spatialreference.org/ref/epsg/" };
    private static final String[] SRS_NAME_PREFIXES = { "urn:ogc:def:crs:EPSG::", "EPSG::", "EPSG:" };
    private static final int DEFAULT_SRID = 4326;
    private static final String EXPECTED_ARRAY = "expected array";
    private static final PrecisionModel DEFAULT_PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING);
    private static final GeometryFactory DEFAULT_GEOMETRY_FACTORY =
            new GeometryFactory(DEFAULT_PRECISION_MODEL, DEFAULT_SRID);

    public GeoJSONDecoder() {
        super(Geometry.class);
    }

    @Override
    public Geometry decodeJSON(JsonNode node, boolean validate)
            throws DecodingException {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        } else {
            if (validate) {
                JSONValidator.getInstance().validateAndThrow(node, SchemaConstants.Common.GEOMETRY);
            }
            return decodeGeometry(node, DEFAULT_GEOMETRY_FACTORY);
        }
    }

    protected Coordinate[] decodeCoordinates(JsonNode node)
            throws GeoJSONDecodingException {
        if (!node.isArray()) {
            throw new GeoJSONDecodingException(EXPECTED_ARRAY);
        }
        Coordinate[] coordinates = new Coordinate[node.size()];
        for (int i = 0; i < node.size(); ++i) {
            coordinates[i] = decodeCoordinate(node.get(i));
        }
        return coordinates;
    }

    protected Polygon decodePolygonCoordinates(JsonNode coordinates, GeometryFactory fac)
            throws GeoJSONDecodingException {
        if (!coordinates.isArray()) {
            throw new GeoJSONDecodingException(EXPECTED_ARRAY);
        }
        if (coordinates.size() < 1) {
            throw new GeoJSONDecodingException("missing polygon shell");
        }
        LinearRing shell = fac.createLinearRing(decodeCoordinates(coordinates.get(0)));
        LinearRing[] holes = new LinearRing[coordinates.size() - 1];
        for (int i = 1; i < coordinates.size(); ++i) {
            holes[i - 1] = fac.createLinearRing(decodeCoordinates(coordinates.get(i)));
        }
        return fac.createPolygon(shell, holes);
    }

    protected Geometry decodeGeometry(Object o, GeometryFactory parentFactory)
            throws GeoJSONDecodingException {
        if (!(o instanceof JsonNode)) {
            throw new GeoJSONDecodingException("Cannot decode " + o);
        }
        final JsonNode node = (JsonNode) o;
        final String type = getType(node);
        final GeometryFactory factory = getGeometryFactory(node, parentFactory);
        if (type.equals(JSONConstants.POINT)) {
            return decodePoint(node, factory);
        } else if (type.equals(JSONConstants.MULTI_POINT)) {
            return decodeMultiPoint(node, factory);
        } else if (type.equals(JSONConstants.LINE_STRING)) {
            return decodeLineString(node, factory);
        } else if (type.equals(JSONConstants.MULTI_LINE_STRING)) {
            return decodeMultiLineString(node, factory);
        } else if (type.equals(JSONConstants.POLYGON)) {
            return decodePolygon(node, factory);
        } else if (type.equals(JSONConstants.MULTI_POLYGON)) {
            return decodeMultiPolygon(node, factory);
        } else if (type.equals(JSONConstants.GEOMETRY_COLLECTION)) {
            return decodeGeometryCollection(node, factory);
        } else {
            throw new GeoJSONDecodingException("Unkown geometry type: " + type);
        }
    }

    protected MultiLineString decodeMultiLineString(JsonNode node, GeometryFactory fac)
            throws GeoJSONDecodingException {
        JsonNode coordinates = requireCoordinates(node);
        LineString[] lineStrings = new LineString[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            JsonNode coords = coordinates.get(i);
            lineStrings[i] = fac.createLineString(decodeCoordinates(coords));
        }
        return fac.createMultiLineString(lineStrings);
    }

    protected LineString decodeLineString(JsonNode node, GeometryFactory fac)
            throws GeoJSONDecodingException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(node));
        return fac.createLineString(coordinates);
    }

    protected MultiPoint decodeMultiPoint(JsonNode node, GeometryFactory fac)
            throws GeoJSONDecodingException {
        Coordinate[] coordinates = decodeCoordinates(requireCoordinates(node));
        return fac.createMultiPoint(coordinates);
    }

    protected Point decodePoint(JsonNode node, GeometryFactory fac)
            throws GeoJSONDecodingException {
        Coordinate parsed = decodeCoordinate(requireCoordinates(node));
        return fac.createPoint(parsed);
    }

    protected Polygon decodePolygon(JsonNode node, GeometryFactory fac)
            throws GeoJSONDecodingException {
        JsonNode coordinates = requireCoordinates(node);
        return decodePolygonCoordinates(coordinates, fac);
    }

    protected MultiPolygon decodeMultiPolygon(JsonNode node, GeometryFactory fac)
            throws GeoJSONDecodingException {
        JsonNode coordinates = requireCoordinates(node);
        Polygon[] polygons = new Polygon[coordinates.size()];
        for (int i = 0; i < coordinates.size(); ++i) {
            polygons[i] = decodePolygonCoordinates(coordinates.get(i), fac);
        }
        return fac.createMultiPolygon(polygons);
    }

    protected GeometryCollection decodeGeometryCollection(JsonNode node, GeometryFactory fac)
            throws GeoJSONDecodingException {
        final JsonNode geometries = node.path(JSONConstants.GEOMETRIES);
        if (!geometries.isArray()) {
            throw new GeoJSONDecodingException("expected 'geometries' array");
        }
        Geometry[] geoms = new Geometry[geometries.size()];
        for (int i = 0; i < geometries.size(); ++i) {
            geoms[i] = decodeGeometry(geometries.get(i), fac);
        }
        return fac.createGeometryCollection(geoms);
    }

    protected GeometryFactory decodeCRS(JsonNode node, GeometryFactory factory)
            throws GeoJSONDecodingException {
        if (!node.path(JSONConstants.CRS).hasNonNull(JSONConstants.TYPE)) {
            throw new GeoJSONDecodingException("Missing CRS type");
        }
        String type = node.path(JSONConstants.CRS).path(JSONConstants.TYPE).textValue();
        JsonNode properties = node.path(JSONConstants.CRS).path(JSONConstants.PROPERTIES);
        if (type.equals(JSONConstants.NAME)) {
            return decodeNamedCRS(properties, factory);
        } else if (type.equals(JSONConstants.LINK)) {
            return decodeLinkedCRS(properties, factory);
        } else {
            throw new GeoJSONDecodingException("Unknown CRS type: " + type);
        }
    }

    protected GeometryFactory decodeNamedCRS(JsonNode properties, GeometryFactory factory)
            throws GeoJSONDecodingException {
        String name = properties.path(JSONConstants.NAME).textValue();
        if (name == null) {
            throw new GeoJSONDecodingException("Missing name attribute for name crs");
        }
        for (String prefix : SRS_NAME_PREFIXES) {
            if (name.startsWith(prefix)) {
                try {
                    int srid = Integer.parseInt(name.substring(prefix.length()));
                    return getGeometryFactory(srid, factory);
                } catch (NumberFormatException e) {
                    throw new GeoJSONDecodingException("Invalid CRS name", e);
                }
            }
        }
        throw new GeoJSONDecodingException("Unsupported named crs: " + name);
    }

    protected GeometryFactory decodeLinkedCRS(JsonNode properties, GeometryFactory factory)
            throws GeoJSONDecodingException {
        String href = properties.path(JSONConstants.HREF).textValue();
        if (href == null) {
            throw new GeoJSONDecodingException("Missing href attribute for link crs");
        }
        for (String prefix : SRS_LINK_PREFIXES) {
            if (href.startsWith(prefix)) {
                try {
                    int srid = Integer.parseInt(href.substring(prefix.length()));
                    return getGeometryFactory(srid, factory);
                } catch (NumberFormatException e) {
                    throw new GeoJSONDecodingException("Invalid CRS link", e);
                }
            }
        }
        throw new GeoJSONDecodingException("Unsupported linked crs: " + href);
    }

    protected Coordinate decodeCoordinate(JsonNode node)
            throws GeoJSONDecodingException {
        if (!node.isArray()) {
            throw new GeoJSONDecodingException(EXPECTED_ARRAY);
        }
        final int dim = node.size();
        if (dim < DIM_2D) {
            throw new GeoJSONDecodingException("coordinates may have at least 2 dimensions");
        }
        if (dim > DIM_3D) {
            throw new GeoJSONDecodingException("coordinates may have at most 3 dimensions");
        }
        final Coordinate coordinate = new Coordinate();
        for (int i = 0; i < dim; ++i) {
            if (node.get(i).isNumber()) {
                coordinate.setOrdinate(i, node.get(i).doubleValue());
            } else {
                throw new GeoJSONDecodingException("coordinate index " + i + " has to be a number");
            }
        }
        return coordinate;
    }

    protected JsonNode requireCoordinates(JsonNode node)
            throws GeoJSONDecodingException {
        if (!node.path(JSONConstants.COORDINATES).isArray()) {
            throw new GeoJSONDecodingException("missing 'coordinates' field");
        }
        return node.path(JSONConstants.COORDINATES);
    }

    protected GeometryFactory getGeometryFactory(JsonNode node, GeometryFactory factory)
            throws GeoJSONDecodingException {
        if (!node.hasNonNull(JSONConstants.CRS)) {
            return factory;
        } else {
            return decodeCRS(node, factory);
        }
    }

    protected GeometryFactory getGeometryFactory(int srid, GeometryFactory factory) {
        if (srid == factory.getSRID()) {
            return factory;
        } else {
            return new GeometryFactory(DEFAULT_PRECISION_MODEL, srid);
        }
    }

    protected String getType(final JsonNode node)
            throws GeoJSONDecodingException {
        if (!node.has(JSONConstants.TYPE)) {
            throw new GeoJSONDecodingException("Can not determine geometry type (missing 'type' field)");
        }
        if (!node.path(JSONConstants.TYPE).isTextual()) {
            throw new GeoJSONDecodingException("'type' field has to be a string");
        }
        return node.path(JSONConstants.TYPE).textValue();
    }

    protected boolean isNumber(JsonNode x) {
        return x == null || !x.isNumber();
    }
}
