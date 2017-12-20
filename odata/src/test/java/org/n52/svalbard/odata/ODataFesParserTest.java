/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.odata;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import org.n52.shetland.ogc.filter.BinaryLogicFilter;
import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.filter.Filter;
import org.n52.shetland.ogc.filter.FilterConstants.BinaryLogicOperator;
import org.n52.shetland.ogc.filter.FilterConstants.ComparisonOperator;
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.svalbard.decode.exception.DecodingException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * @author Christian Autermann
 */
public class ODataFesParserTest {

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    private ODataFesParser parser;
    private Polygon polygon;
    private GeometryFactory geometryFactory;
    private String wktGeometry;

    @Before
    public void setup() {
        this.parser = new ODataFesParser();
        this.geometryFactory = new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING_SINGLE), 4326);
        this.polygon = this.geometryFactory.createPolygon(new Coordinate[] {
            new Coordinate(-15.46, 77.98),
            new Coordinate(-93.51, 38.27),
            new Coordinate(47.10, -1.05),
            new Coordinate(58.71, 70.61),
            new Coordinate(-15.46, 77.98)
        });
        this.wktGeometry = new WKTWriter().write(polygon).replaceFirst(" ", "").replaceAll(", ", ",");
    }

    @Test
    public void testFeatureOfInterestIdentifier() throws Exception {
        Filter<?> filter = parser.decode("featureOfInterest eq '213'");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:featureOfInterest"));
    }

    @Test
    public void testFeatureOfInterestIdentifier2() throws Exception {
        Filter<?> filter = parser.decode("featureOfInterest/id eq '213'");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:featureOfInterest"));
    }

    @Test
    public void testId() throws Exception {
        Filter<?> filter = parser.decode("id eq '213'");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("gml:identifier"));
    }

    @Test
    public void testIdentifier() throws Exception {
        Filter<?> filter = parser.decode("identifier eq '213'");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("gml:identifier"));
    }

    @Test
    public void testResultEqString() throws Exception {
        Filter<?> filter = parser.decode("result eq 'a213'");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("a213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:result"));
    }

    @Test
    public void testValuesEqString() throws Exception {
        Filter<?> filter = parser.decode("values eq '213'");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:result"));
    }

    @Test
    public void testTextValueEqString() throws Exception {
        Filter<?> filter = parser.decode("textValue eq '213'");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:result"));
    }
// >=4.0.0 <4.2.0

    @Test
    public void testValuesEqInteger() throws Exception {
        Filter<?> filter = parser.decode("values eq 213");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:result"));
    }

    @Test
    public void testNumericValueEqInteger() throws Exception {
        Filter<?> filter = parser.decode("numericValue eq 213");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:result"));
    }

    @Test
    public void testCountValueEqInteger() throws Exception {
        Filter<?> filter = parser.decode("countValue eq 213");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:result"));
    }

    @Test
    public void testNumericValueEqDecimal() throws Exception {
        Filter<?> filter = parser.decode("numericValue eq 213.12");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213.12"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:result"));
    }

    @Test
    public void testCountValueEqDecimal() throws Exception {
        Filter<?> filter = parser.decode("countValue eq 213.12");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        errors.checkThat(((ComparisonFilter) filter).getValue(), is("213.12"));
        errors.checkThat(((ComparisonFilter) filter).getValueReference(), is("om:result"));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testConjunction() throws Exception {
        Filter<?> filter = parser.decode("countValue lt 10 and textValue eq 'thetext'");
        assertThat(filter, is(instanceOf(BinaryLogicFilter.class)));
        BinaryLogicFilter blf = (BinaryLogicFilter) filter;
        errors.checkThat(blf.getOperator(), is(BinaryLogicOperator.And));
        Set<Filter<?>> filterPredicates = blf.getFilterPredicates();

        errors.checkThat(filterPredicates, Matchers.containsInAnyOrder(
                         Matchers.instanceOf(ComparisonFilter.class),
                         Matchers.instanceOf(ComparisonFilter.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDisjunction() throws Exception {
        Filter<?> filter = parser.decode("countValue lt 10 or textValue eq 'thetext'");
        assertThat(filter, is(instanceOf(BinaryLogicFilter.class)));
        BinaryLogicFilter blf = (BinaryLogicFilter) filter;
        errors.checkThat(blf.getOperator(), is(BinaryLogicOperator.Or));
        Set<Filter<?>> filterPredicates = blf.getFilterPredicates();
        errors.checkThat(filterPredicates, Matchers.containsInAnyOrder(
                         Matchers.instanceOf(ComparisonFilter.class),
                         Matchers.instanceOf(ComparisonFilter.class)));
    }

    @Test
    public void testContains() throws DecodingException {
        Filter<?> filter = parser.decode("contains(textValue,'as%df')");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        ComparisonFilter cf = (ComparisonFilter) filter;
        errors.checkThat(cf.getOperator(), is(ComparisonOperator.PropertyIsLike));
        errors.checkThat(cf.getValue(), is("%as%df%"));
        errors.checkThat(cf.getValueReference(), is("om:result"));
    }

    @Test
    public void testStartswith() throws DecodingException {
        Filter<?> filter = parser.decode("startswith(textValue,'asdf')");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        ComparisonFilter cf = (ComparisonFilter) filter;
        errors.checkThat(cf.getOperator(), is(ComparisonOperator.PropertyIsLike));
        errors.checkThat(cf.getValue(), is("asdf%"));
        errors.checkThat(cf.getValueReference(), is("om:result"));
    }

    @Test
    public void testEndswith() throws DecodingException {
        Filter<?> filter = parser.decode("endswith(textValue,'asdf')");
        assertThat(filter, is(instanceOf(ComparisonFilter.class)));
        ComparisonFilter cf = (ComparisonFilter) filter;
        errors.checkThat(cf.getOperator(), is(ComparisonOperator.PropertyIsLike));
        errors.checkThat(cf.getValue(), is("%asdf"));
        errors.checkThat(cf.getValueReference(), is("om:result"));
    }

    @Test
    public void testFeatureOfInterestShapeGeoIntersectsPolygon() throws Exception {

        Filter<?> filter = parser.decode(String
                // >=4.2.0
                //              .format("geo.intersects(geometry,geometry'SRID=%s;%s')",
                // >=4.0.0 <4.2.0
                .format("geo.intersects(featureOfInterest/shape,'SRID=%s;%s')",
                        polygon.getSRID(), wktGeometry));

        assertThat(filter, is(instanceOf(SpatialFilter.class)));
        SpatialFilter sf = (SpatialFilter) filter;
        errors.checkThat(sf.getSrid(), is(4326));
        errors.checkThat(sf.getGeometry().isEnvelope(), is(false));
        errors.checkThat(sf.getGeometry().isGeometry(), is(true));
        errors.checkThat(sf.getGeometry().getGeometry().get(), is(instanceOf(Polygon.class)));
        errors.checkThat(sf.getValueReference(), is("om:featureOfInterest/*/sams:shape"));
    }

     @Test
    public void testFeatureOfInterestGeoIntersectsPolygon() throws Exception {

        Filter<?> filter = parser.decode(String
                // >=4.2.0
                //              .format("geo.intersects(geometry,geometry'SRID=%s;%s')",
                // >=4.0.0 <4.2.0
                .format("geo.intersects(featureOfInterest,'SRID=%s;%s')",
                        polygon.getSRID(), wktGeometry));

        assertThat(filter, is(instanceOf(SpatialFilter.class)));
        SpatialFilter sf = (SpatialFilter) filter;
        errors.checkThat(sf.getSrid(), is(4326));
        errors.checkThat(sf.getGeometry().isEnvelope(), is(false));
        errors.checkThat(sf.getGeometry().isGeometry(), is(true));
        errors.checkThat(sf.getGeometry().getGeometry().get(), is(instanceOf(Polygon.class)));
        errors.checkThat(sf.getValueReference(), is("om:featureOfInterest/*/sams:shape"));
    }

    @Test
    public void testSamplingGeometryGeoIntersectsPolygon() throws Exception {

        Filter<?> filter = parser.decode(String
                // >=4.2.0
                //              .format("geo.intersects(geometry,geometry'SRID=%s;%s')",
                // >=4.0.0 <4.2.0
                .format("geo.intersects(samplingGeometry,'SRID=%s;%s')",
                        polygon.getSRID(), wktGeometry));

        assertThat(filter, is(instanceOf(SpatialFilter.class)));
        SpatialFilter sf = (SpatialFilter) filter;
        errors.checkThat(sf.getSrid(), is(4326));
        errors.checkThat(sf.getGeometry().isEnvelope(), is(false));
        errors.checkThat(sf.getGeometry().isGeometry(), is(true));
        errors.checkThat(sf.getGeometry().getGeometry().get(), is(instanceOf(Polygon.class)));
        errors.checkThat(sf.getValueReference(), is("http://www.opengis.net/req/omxml/2.0/data/samplingGeometry"));
    }

}
