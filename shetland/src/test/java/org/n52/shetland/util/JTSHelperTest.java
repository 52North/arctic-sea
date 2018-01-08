/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.n52.shetland.util.JTSHelperForTesting.randomCoordinate;
import static org.n52.shetland.util.JTSHelperForTesting.randomCoordinateRing;
import static org.n52.shetland.util.JTSHelperForTesting.randomCoordinates;

import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 4.0.0
 */
public class JTSHelperTest extends JTSHelper {

    @Test
    public void factoryFromSridShouldSetSrid() {
        GeometryFactory factory = getGeometryFactoryForSRID(4326);
        assertThat(factory, is(notNullValue()));
        Geometry g = factory.createPoint(new Coordinate(1, 2));
        assertThat(g, is(notNullValue()));
        assertThat(g.getSRID(), is(4326));
    }

    @Test
    public void factoryFromGeometryShouldSetSrid() {
        GeometryFactory factory = getGeometryFactoryForSRID(4326);
        assertThat(factory, is(notNullValue()));
        Geometry g = factory.createPoint(new Coordinate(1, 2));
        factory = getGeometryFactory(g);
        assertThat(factory, is(notNullValue()));
        g = factory.createPoint(new Coordinate(1, 2));
        assertThat(g, is(notNullValue()));
        assertThat(g.getSRID(), is(4326));
    }

    @Test
    public void shouldPointWKTString() throws OwsExceptionReport, ParseException {
        String coordinates = "52.0 7.0";
        StringBuilder builder = new StringBuilder();
        builder.append(WKT_POINT);
        builder.append("(");
        builder.append(coordinates);
        builder.append(")");
        assertEquals(builder.toString(), createWKTPointFromCoordinateString(coordinates));
        assertEquals(createGeometryFromWKT(builder.toString(), 4326),
                createGeometryFromWKT(createWKTPointFromCoordinateString(coordinates), 4326));
    }

    @Test
    public void shouldReverseLinearRing() throws OwsExceptionReport {
        testReverse(getGeometryFactoryForSRID(4326).createLinearRing(randomCoordinateRing(10)));
    }

    @Test
    public void shouldReversePoint() throws OwsExceptionReport {
        testReverse(getGeometryFactoryForSRID(4326).createPoint(randomCoordinate()));
    }

    @Test
    public void shouldReverseLineString() throws OwsExceptionReport {
        testReverse(getGeometryFactoryForSRID(4326).createLineString(randomCoordinates(10)));
    }

    @Test
    public void shouldReversePolygon() throws OwsExceptionReport {
        final GeometryFactory factory = getGeometryFactoryForSRID(4326);
        testReverse(factory.createPolygon(
                factory.createLinearRing(randomCoordinateRing(10)),
                new LinearRing[] { factory.createLinearRing(randomCoordinateRing(10)),
                        factory.createLinearRing(randomCoordinateRing(41)),
                        factory.createLinearRing(randomCoordinateRing(13)) }));
    }

    @Test
    public void shouldReverseMultiPoint() throws OwsExceptionReport {
        final GeometryFactory factory = getGeometryFactoryForSRID(4326);
        testReverse(factory.createMultiPointFromCoords(randomCoordinates(20)));
    }

    @Test
    public void shouldReverseMultiLineString() throws OwsExceptionReport {
        final GeometryFactory factory = getGeometryFactoryForSRID(4326);
        testReverse(factory.createMultiLineString(new LineString[] {
                factory.createLineString(randomCoordinateRing(21)),
                factory.createLineString(randomCoordinateRing(21)),
                factory.createLineString(randomCoordinateRing(15)), }));
    }

    @Test
    public void shouldReverseMultiPolygon() throws OwsExceptionReport {
        final GeometryFactory factory = getGeometryFactoryForSRID(4326);
        testReverse(factory.createMultiPolygon(new Polygon[] {
                factory.createPolygon(
                        factory.createLinearRing(randomCoordinateRing(13)),
                        new LinearRing[] { factory.createLinearRing(randomCoordinateRing(130)),
                                factory.createLinearRing(randomCoordinateRing(4121)),
                                factory.createLinearRing(randomCoordinateRing(12)) }),
                factory.createPolygon(
                        factory.createLinearRing(randomCoordinateRing(8)),
                        new LinearRing[] { factory.createLinearRing(randomCoordinateRing(1101)),
                                factory.createLinearRing(randomCoordinateRing(413)),
                                factory.createLinearRing(randomCoordinateRing(123)) }),
                factory.createPolygon(
                        factory.createLinearRing(randomCoordinateRing(89)),
                        new LinearRing[] { factory.createLinearRing(randomCoordinateRing(112)),
                                factory.createLinearRing(randomCoordinateRing(4)),
                                factory.createLinearRing(randomCoordinateRing(43)) }) }));
    }

    @Test
    public void shouldReverseGeometryCollection() throws OwsExceptionReport {
        final GeometryFactory factory = getGeometryFactoryForSRID(4326);
        testReverse(factory.createGeometryCollection(new Geometry[] {
                factory.createMultiPolygon(new Polygon[] {
                        factory.createPolygon(
                                factory.createLinearRing(randomCoordinateRing(13)),
                                new LinearRing[] { factory.createLinearRing(randomCoordinateRing(130)),
                                        factory.createLinearRing(randomCoordinateRing(4121)),
                                        factory.createLinearRing(randomCoordinateRing(12)) }),
                        factory.createPolygon(
                                factory.createLinearRing(randomCoordinateRing(8)),
                                new LinearRing[] { factory.createLinearRing(randomCoordinateRing(1101)),
                                        factory.createLinearRing(randomCoordinateRing(413)),
                                        factory.createLinearRing(randomCoordinateRing(123)) }),
                        factory.createPolygon(
                                factory.createLinearRing(randomCoordinateRing(89)),
                                new LinearRing[] { factory.createLinearRing(randomCoordinateRing(112)),
                                        factory.createLinearRing(randomCoordinateRing(4)),
                                        factory.createLinearRing(randomCoordinateRing(43)) }) }),
                factory.createMultiLineString(new LineString[] { factory.createLineString(randomCoordinateRing(21)),
                        factory.createLineString(randomCoordinateRing(21)),
                        factory.createLineString(randomCoordinateRing(15)), }),
                factory.createPolygon(
                        factory.createLinearRing(randomCoordinateRing(10)),
                        new LinearRing[] { factory.createLinearRing(randomCoordinateRing(10)),
                                factory.createLinearRing(randomCoordinateRing(41)),
                                factory.createLinearRing(randomCoordinateRing(13)) }),
                getGeometryFactoryForSRID(4326).createLineString(randomCoordinates(10)),
                getGeometryFactoryForSRID(4326).createLineString(randomCoordinates(10)) }));
    }

    @Test
    public void shouldReverseUnknownGeometry() throws OwsExceptionReport {
        testReverse(new UnknownGeometry(getGeometryFactoryForSRID(4326).createLineString(randomCoordinates(5))));
    }

    protected void testReverse(Geometry geometry) throws OwsExceptionReport {
        Geometry reversed = switchCoordinateAxisOrder(geometry);
        assertThat(reversed, is(instanceOf(geometry.getClass())));
        assertThat(reversed, is(not(sameInstance(geometry))));
        assertThat(reversed, is(notNullValue()));
        assertThat(reversed, is(ReverseOf.reverseOf(geometry)));
    }

}
