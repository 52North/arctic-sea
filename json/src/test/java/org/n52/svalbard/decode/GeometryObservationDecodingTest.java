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

import static com.github.fge.jackson.JsonLoader.fromResource;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.n52.shetland.util.DateTimeHelper.parseIsoString2DateTime;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.SingleObservationValue;
import org.n52.shetland.ogc.om.values.GeometryValue;
import org.n52.svalbard.ConfiguredSettingsManager;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.ObservationDecoder;

import com.fasterxml.jackson.databind.JsonNode;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class GeometryObservationDecodingTest {
    @ClassRule
    public static final ConfiguredSettingsManager csm = new ConfiguredSettingsManager();

    private static DateTime phenomenonTime;

    private static JsonNode json;

    private ObservationDecoder decoder;

    private OmObservation observation;

    @BeforeClass
    public static void beforeClass() {
        try {
            json = fromResource("/examples/geometry-observation.json");
            phenomenonTime = parseIsoString2DateTime("2013-01-01T00:00:00+02:00");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Before
    public void before()
            throws DecodingException {
        this.decoder = new ObservationDecoder();
        this.observation = decoder.decodeJSON(json, true);
    }

    @Test
    public void testObservation() {
        assertThat(observation, is(notNullValue()));
        final String type = observation.getObservationConstellation().getObservationType();
        assertThat(type, is(equalTo(OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION)));
        final ObservationValue<?> value = observation.getValue();
        assertThat(value, is(instanceOf(SingleObservationValue.class)));
        assertThat(value.getPhenomenonTime(), is(instanceOf(TimeInstant.class)));
        TimeInstant pt = (TimeInstant) value.getPhenomenonTime();
        assertThat(pt.getValue(), is(equalTo(phenomenonTime)));
        assertThat(value.getValue(), is(instanceOf(GeometryValue.class)));
        GeometryValue v = (GeometryValue) value.getValue();
        assertThat(v.getUnit(), is(nullValue()));
        Geometry g = v.getValue();
        assertThat(g, is(instanceOf(LineString.class)));
        assertThat(g.getSRID(), is(2000));
        assertThat(g.getNumPoints(), is(2));
        assertThat(g.getCoordinates()[0], is(equalTo(new Coordinate(52, 7))));
        assertThat(g.getCoordinates()[1], is(equalTo(new Coordinate(51, 7))));
    }
}
