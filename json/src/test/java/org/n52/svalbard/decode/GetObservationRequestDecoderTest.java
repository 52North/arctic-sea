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
package org.n52.svalbard.decode;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.n52.shetland.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.shetland.ogc.filter.FilterConstants.TimeOperator;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.sos.request.GetObservationRequest;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.GetObservationRequestDecoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.vividsolutions.jts.geom.Point;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@Ignore
public class GetObservationRequestDecoderTest {
//    @ClassRule
//    public static final ConfiguredSettingsManager csm = new ConfiguredSettingsManager();

    private GetObservationRequestDecoder decoder;

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Before
    public void before() {
        this.decoder = new GetObservationRequestDecoder();
    }

    @Test
    public void hasRequest()
            throws IOException, DecodingException {
        assertThat(loadSingle().getOperationName(), is(equalTo("GetObservation")));
    }

    @Test
    public void hasVersion()
            throws IOException, DecodingException {
        assertThat(loadSingle().getVersion(), is(equalTo("2.0.0")));
    }

    @Test
    public void hasService()
            throws IOException, DecodingException {
        assertThat(loadSingle().getService(), is(equalTo("SOS")));
    }

    @Test
    public void hasProcedure()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadSingle();
        assertThat(req.getProcedures(), is(notNullValue()));
        assertThat(req.getProcedures(), hasSize(1));
        assertThat(req.getProcedures().get(0), is(notNullValue()));
        assertThat(req.getProcedures().get(0), is(equalTo("procedure1")));
    }

    @Test
    public void hasProcedures()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadMultiple();
        assertThat(req.getProcedures(), is(notNullValue()));
        assertThat(req.getProcedures(), hasSize(2));
        assertThat(req.getProcedures().get(0), is(notNullValue()));
        assertThat(req.getProcedures().get(0), is(equalTo("procedure1")));
        assertThat(req.getProcedures().get(1), is(notNullValue()));
        assertThat(req.getProcedures().get(1), is(equalTo("procedure2")));

    }

    @Test
    public void hasOffering()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadSingle();
        assertThat(req.getOfferings(), is(notNullValue()));
        assertThat(req.getOfferings(), hasSize(1));
        assertThat(req.getOfferings().get(0), is(notNullValue()));
        assertThat(req.getOfferings().get(0), is(equalTo("offering1")));
    }

    @Test
    public void hasOfferings()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadMultiple();
        assertThat(req.getOfferings(), is(notNullValue()));
        assertThat(req.getOfferings(), hasSize(2));
        assertThat(req.getOfferings().get(0), is(notNullValue()));
        assertThat(req.getOfferings().get(0), is(equalTo("offering1")));
        assertThat(req.getOfferings().get(1), is(notNullValue()));
        assertThat(req.getOfferings().get(1), is(equalTo("offering2")));

    }

    @Test
    public void hasObservedProperty()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadSingle();
        assertThat(req.getObservedProperties(), is(notNullValue()));
        assertThat(req.getObservedProperties(), hasSize(1));
        assertThat(req.getObservedProperties().get(0), is(notNullValue()));
        assertThat(req.getObservedProperties().get(0), is(equalTo("observedProperty1")));
    }

    @Test
    public void hasObservedProperties()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadMultiple();
        assertThat(req.getObservedProperties(), is(notNullValue()));
        assertThat(req.getObservedProperties(), hasSize(2));
        assertThat(req.getObservedProperties().get(0), is(notNullValue()));
        assertThat(req.getObservedProperties().get(0), is(equalTo("observedProperty1")));
        assertThat(req.getObservedProperties().get(1), is(notNullValue()));
        assertThat(req.getObservedProperties().get(1), is(equalTo("observedProperty2")));
    }

    @Test
    public void hasFeatureOfInterest()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadSingle();
        assertThat(req.getFeatureIdentifiers(), is(notNullValue()));
        assertThat(req.getFeatureIdentifiers(), hasSize(1));
        assertThat(req.getFeatureIdentifiers().get(0), is(notNullValue()));
        assertThat(req.getFeatureIdentifiers().get(0), is(equalTo("featureOfInterest1")));
    }

    @Test
    public void hasFeaturesOfInterest()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadMultiple();
        assertThat(req.getFeatureIdentifiers(), is(notNullValue()));
        assertThat(req.getFeatureIdentifiers(), hasSize(2));
        assertThat(req.getFeatureIdentifiers().get(0), is(notNullValue()));
        assertThat(req.getFeatureIdentifiers().get(0), is(equalTo("featureOfInterest1")));
        assertThat(req.getFeatureIdentifiers().get(1), is(notNullValue()));
        assertThat(req.getFeatureIdentifiers().get(1), is(equalTo("featureOfInterest2")));
    }

    @Test
    public void hasSpatialFilter()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadSingle();
        assertThat(req.getSpatialFilter(), is(notNullValue()));
        assertThat(req.getSpatialFilter().getOperator(), is(SpatialOperator.Equals));
        assertThat(req.getSpatialFilter().getValueReference(),
                is("om:featureOfInterest/sams:SF_SpatialSamplingFeature/sams:shape"));
        assertThat(req.getSpatialFilter().getGeometry(), is(notNullValue()));
        assertThat(req.getSpatialFilter().getGeometry(), is(instanceOf(Point.class)));
        assertThat(req.getSpatialFilter().getGeometry().getSRID(), is(4326));
        assertThat(req.getSpatialFilter().getGeometry().getCoordinate().x, is(51.0));
        assertThat(req.getSpatialFilter().getGeometry().getCoordinate().y, is(8.0));
        assertThat(Double.isNaN(req.getSpatialFilter().getGeometry().getCoordinate().z), is(true));
    }

    @Test
    public void hasNoTemporalFilters()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadNoFilters();
        assertThat(req.getTemporalFilters(), is(notNullValue()));
        assertThat(req.getTemporalFilters(), hasSize(0));
    }

    @Test
    public void hasSingleTemporalFilter()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadSingle();
        assertThat(req.getTemporalFilters(), is(notNullValue()));
        assertThat(req.getTemporalFilters(), hasSize(1));
        assertThat(req.getTemporalFilters().get(0), is(notNullValue()));
        assertThat(req.getTemporalFilters().get(0).getOperator(), is(TimeOperator.TM_Equals));
        assertThat(req.getTemporalFilters().get(0).getValueReference(), is("om:phenomenonTime"));
        assertThat(req.getTemporalFilters().get(0).getTime(), is(instanceOf(TimePeriod.class)));
        final TimePeriod time = (TimePeriod) req.getTemporalFilters().get(0).getTime();
        assertThat(time.getStart(),
                is(equalTo(new DateTime(2013, 01, 01, 00, 00, 00, 00, DateTimeZone.forOffsetHours(2)))));
        assertThat(time.getEnd(),
                is(equalTo(new DateTime(2013, 01, 01, 01, 00, 00, 00, DateTimeZone.forOffsetHours(2)))));
    }

    @Test
    public void hasMultipleTemporalFilters()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadMultiple();
        assertThat(req.getTemporalFilters(), is(notNullValue()));
        assertThat(req.getTemporalFilters(), hasSize(2));
        assertThat(req.getTemporalFilters().get(0), is(notNullValue()));
        assertThat(req.getTemporalFilters().get(0).getOperator(), is(TimeOperator.TM_Equals));
        assertThat(req.getTemporalFilters().get(0).getValueReference(), is("om:phenomenonTime"));
        assertThat(req.getTemporalFilters().get(0).getTime(), is(instanceOf(TimePeriod.class)));
        final TimePeriod time1 = (TimePeriod) req.getTemporalFilters().get(0).getTime();
        assertThat(time1.getStart(),
                is(equalTo(new DateTime(2013, 01, 01, 00, 00, 00, 00, DateTimeZone.forOffsetHours(2)))));
        assertThat(time1.getEnd(),
                is(equalTo(new DateTime(2013, 01, 01, 01, 00, 00, 00, DateTimeZone.forOffsetHours(2)))));
        assertThat(req.getTemporalFilters().get(1), is(notNullValue()));
        assertThat(req.getTemporalFilters().get(1).getOperator(), is(TimeOperator.TM_Equals));
        assertThat(req.getTemporalFilters().get(1).getValueReference(), is("om:phenomenonTime"));
        assertThat(req.getTemporalFilters().get(1).getTime(), is(instanceOf(TimePeriod.class)));
        final TimePeriod time2 = (TimePeriod) req.getTemporalFilters().get(1).getTime();
        assertThat(time2.getStart(),
                is(equalTo(new DateTime(2013, 01, 01, 20, 00, 00, 00, DateTimeZone.forOffsetHours(2)))));
        assertThat(time2.getEnd(),
                is(equalTo(new DateTime(2013, 01, 01, 22, 00, 00, 00, DateTimeZone.forOffsetHours(2)))));
    }

    @Test
    public void hasMergeObservationsIntoDataArrayExtension()
            throws IOException, DecodingException {
        final GetObservationRequest req = loadMergeIntoArray();
        assertThat(req.getBooleanExtension("MergeObservationsIntoDataArray"), is(true));
    }

    protected GetObservationRequest loadSingle()
            throws DecodingException, IOException {
        final JsonNode json = JsonLoader.fromResource("/examples/sos/GetObservationRequest-single.json");
        final GetObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req, is(notNullValue()));
        return req;
    }

    protected GetObservationRequest loadMultiple()
            throws DecodingException, IOException {
        final JsonNode json = JsonLoader.fromResource("/examples/sos/GetObservationRequest-multiple.json");
        final GetObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req, is(notNullValue()));
        return req;
    }

    protected GetObservationRequest loadNoFilters()
            throws DecodingException, IOException {
        final JsonNode json = JsonLoader.fromResource("/examples/sos/GetObservationRequest-no-filters.json");
        final GetObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req, is(notNullValue()));
        return req;
    }

    protected GetObservationRequest loadMergeIntoArray()
            throws DecodingException, IOException {
        final JsonNode json = JsonLoader.fromResource("/examples/sos/GetObservationRequest-merge-into-array.json");
        final GetObservationRequest req = decoder.decodeJSON(json, true);
        assertThat(req, is(notNullValue()));
        return req;
    }
}
