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
package org.n52.svalbard.decode.json;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.n52.shetland.util.DateTimeHelper.parseIsoString2DateTime;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.gml.time.Time;
import org.n52.shetland.ogc.gml.time.TimeInstant;
import org.n52.shetland.ogc.gml.time.TimePeriod;
import org.n52.shetland.ogc.om.AbstractPhenomenon;
import org.n52.shetland.ogc.om.ObservationValue;
import org.n52.shetland.ogc.om.OmConstants;
import org.n52.shetland.ogc.om.OmObservation;
import org.n52.shetland.ogc.om.OmObservationConstellation;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.om.values.QuantityValue;
import org.n52.svalbard.ConfiguredSettingsManager;
import org.n52.svalbard.coding.json.JSONConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.JSONDecodingException;
import org.n52.svalbard.decode.json.ObservationDecoder;
import org.n52.svalbard.encode.exception.EncodingException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@ExtendWith(ConfiguredSettingsManager.class)
public class MeasurementDecodingTest {

    public static final String PROCEDURE = "http://52north.org/example/procedure/1";

    public static final String OBSERVED_PROPERTY = "http://52north.org/example/observedProperty/1";

    public static final String UNKNOWN_CODESPACE = "http://www.opengis.net/def/nil/OGC/0/unknown";

    public static final String FEATURE_NAME = "feature1";

    public static final String FEATURE_IDENTIFIER = "feature1";

    public static final String IDENTIFIER = "measurement1";

    private static JsonNode json;

    private static DateTime resultTime;

    private static DateTime validTimeEnd;

    private static DateTime validTimeStart;

    private static DateTime phenomenonTime;

    private ObservationDecoder decoder;

    private OmObservation observation;

    @BeforeAll
    public static void beforeClass() {
        try {
            resultTime = parseIsoString2DateTime("2013-01-01T00:00:00+02:00");
            validTimeStart = parseIsoString2DateTime("2013-01-01T00:00:00+02:00");
            validTimeEnd = parseIsoString2DateTime("2013-01-01T01:00:00+02:00");
            phenomenonTime = parseIsoString2DateTime("2013-01-01T00:00:00+02:00");
            json = JsonLoader.fromResource("/examples/measurement-geometry-inline.json");
        } catch (Exception ex) {
            throw new RuntimeException();
        }
    }

    @BeforeEach
    public void before()
            throws DecodingException {
        this.decoder = new ObservationDecoder();
        this.observation = decoder.decodeJSON(json, true);
    }

    @Test
    public void testObservation() {
        assertThat(observation, is(notNullValue()));
    }

    @Test
    public void testResultTime() {
        assertThat(observation, is(notNullValue()));
        final TimeInstant rt = observation.getResultTime();
        assertThat(rt, is(notNullValue()));
        assertThat(rt.getValue(), is(equalTo(resultTime)));
    }

    @Test
    public void testIdentifier() {
        assertThat(observation, is(notNullValue()));
        final CodeWithAuthority cwa = observation.getIdentifierCodeWithAuthority();
        assertThat(cwa, is(notNullValue()));
        assertThat(cwa.getValue(), is(equalTo(IDENTIFIER)));
        assertThat(cwa.getCodeSpace(), is(equalTo(UNKNOWN_CODESPACE)));
    }

    @Test
    public void testPhenomenonTime() {
        assertThat(observation, is(notNullValue()));
        final ObservationValue<?> ov = observation.getValue();
        assertThat(ov, is(notNullValue()));
        final Time pt = ov.getPhenomenonTime();
        assertThat(pt, is(notNullValue()));
        assertThat(pt, is(instanceOf(TimeInstant.class)));
        TimeInstant ti = (TimeInstant) pt;
        assertThat(ti.getValue(), is(equalTo(phenomenonTime)));
    }

    @Test
    public void testValue() {
        assertThat(observation, is(notNullValue()));
        final ObservationValue<?> ov = observation.getValue();
        assertThat(ov, is(notNullValue()));
        assertThat(ov.getValue(), is(instanceOf(QuantityValue.class)));
        QuantityValue qv = (QuantityValue) ov.getValue();
        assertThat(qv.getUnit(), is(equalTo("testunit1")));
        assertThat(qv.getValue(), is(equalTo(BigDecimal.valueOf(123123.0))));
    }

    @Test
    public void testValidTime() {
        assertThat(observation, is(notNullValue()));
        final TimePeriod vt = observation.getValidTime();
        assertThat(vt, is(notNullValue()));
        assertThat(vt.getStart(), is(equalTo(validTimeStart)));
        assertThat(vt.getEnd(), is(equalTo(validTimeEnd)));
    }

    @Test
    public void testObservationConstellation() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
    }

    @Test
    public void testObservationType() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
        assertThat(oc.getObservationType(), is(equalTo(OmConstants.OBS_TYPE_MEASUREMENT)));
    }

    @Test
    public void testProcedure() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
        final AbstractFeature p = oc.getProcedure();
        assertThat(p, is(notNullValue()));
        assertThat(p.getIdentifier(), is(equalTo(PROCEDURE)));
    }

    @Test
    public void testObservedProperty() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
        final AbstractPhenomenon op = oc.getObservableProperty();
        assertThat(op, is(notNullValue()));
        assertThat(op.getIdentifier(), is(equalTo(OBSERVED_PROPERTY)));
    }

    @Test
    public void testFeatureOfInterest() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
        final AbstractFeature foi = oc.getFeatureOfInterest();
        assertThat(foi, is(notNullValue()));
    }

    @Test
    public void testFeatureOfInterestSampledFeatures() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
        final AbstractFeature foi = oc.getFeatureOfInterest();
        assertThat(foi, is(notNullValue()));
        assertThat(foi, is(instanceOf(SamplingFeature.class)));
        SamplingFeature sf = (SamplingFeature) foi;
        assertThat(sf.getSampledFeatures(), is(notNullValue()));
        assertThat(sf.getSampledFeatures().size(), is(3));

        final AbstractFeature af1 = sf.getSampledFeatures().get(0);
        assertThat(af1, is(notNullValue()));
        assertThat(af1.getIdentifierCodeWithAuthority(), is(notNullValue()));
        assertThat(af1.getIdentifierCodeWithAuthority().getValue(), is(equalTo("sampledFeature1")));

        final AbstractFeature af2 = sf.getSampledFeatures().get(1);
        assertThat(af2, is(notNullValue()));
        assertThat(af2.getIdentifierCodeWithAuthority(), is(notNullValue()));
        assertThat(af2.getIdentifierCodeWithAuthority().getValue(), is(equalTo("sampledFeature2")));
        assertThat(af2.getName(), is(notNullValue()));
        assertThat(af2.getName().size(), is(1));
        assertThat(af2.getName().get(0), is(not(nullValue())));
        assertThat(af2.getName().get(0).isSetCodeSpace(), is(false));
        assertThat(af2.getName().get(0).getValue(), is(equalTo("sampledFeature2")));
        assertThat(af2, is(instanceOf(SamplingFeature.class)));
        SamplingFeature sf2 = (SamplingFeature) af2;
        assertThat(sf2.getGeometry(), is(notNullValue()));
        assertThat(sf2.getGeometry().getCoordinate().x, is(51.0));
        assertThat(sf2.getGeometry().getCoordinate().y, is(8.0));

        final AbstractFeature af3 = sf.getSampledFeatures().get(2);
        assertThat(af3, is(notNullValue()));
        assertThat(af3.getIdentifierCodeWithAuthority(), is(notNullValue()));
        assertThat(af3.getIdentifierCodeWithAuthority().getValue(), is(equalTo("sampledFeature3")));
    }

    @Test
    public void testFeatureOfInterestIdentifier() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
        final AbstractFeature foi = oc.getFeatureOfInterest();
        assertThat(foi, is(notNullValue()));
        assertThat(foi.getIdentifierCodeWithAuthority(), is(notNullValue()));
        assertThat(foi.getIdentifierCodeWithAuthority().getCodeSpace(), is(equalTo(UNKNOWN_CODESPACE)));
        assertThat(foi.getIdentifierCodeWithAuthority().getValue(), is(equalTo(FEATURE_IDENTIFIER)));
    }

    @Test
    public void testFeatureOfInterestName() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
        final AbstractFeature foi = oc.getFeatureOfInterest();
        assertThat(foi, is(notNullValue()));
        final List<CodeType> name = foi.getName();
        assertThat(name, is(notNullValue()));
        assertThat(name.size(), is(3));
        assertThat(name.get(0), is(notNullValue()));
        assertThat(name.get(0).getValue(), is(equalTo(FEATURE_NAME)));
        assertThat(name.get(0).getCodeSpace().toString(), is(equalTo("http://x.y/z")));
        assertThat(name.get(1), is(notNullValue()));
        assertThat(name.get(1).getValue(), is(equalTo("othername1")));
        assertThat(name.get(1).isSetCodeSpace(), is(false));
        assertThat(name.get(2), is(notNullValue()));
        assertThat(name.get(2).getValue(), is(equalTo("othername2")));
        assertThat(name.get(2).isSetCodeSpace(), is(false));
    }

    @Test
    public void testFeatureOfInterestGeometry() {
        assertThat(observation, is(notNullValue()));
        final OmObservationConstellation oc = observation.getObservationConstellation();
        assertThat(oc, is(notNullValue()));
        assertThat(oc.getFeatureOfInterest(), is(notNullValue()));
        assertThat(oc.getFeatureOfInterest(), is(instanceOf(SamplingFeature.class)));
        SamplingFeature foi = (SamplingFeature) oc.getFeatureOfInterest();
        assertThat(foi.getGeometry(), is(notNullValue()));
        assertThat(foi.getGeometry(), is(instanceOf(Point.class)));
        assertThat(foi.getGeometry().getCoordinate().x, is(52.0));
        assertThat(foi.getGeometry().getCoordinate().y, is(7.0));
    }

    @Test
    public void testNull()
            throws DecodingException {
        assertThat(decoder.decode(null), is(nullValue()));
    }

    @Test
    public void testUnknownObservationType()
            throws DecodingException {
        final String type = "someType";
        final ObjectNode c = json.deepCopy();
        c.put(JSONConstants.TYPE, type);

        JSONDecodingException assertThrows = assertThrows(JSONDecodingException.class, () -> {
            decoder.decode(c);
        });
        assertEquals("Unsupported observationType: " + type, assertThrows.getMessage());
    }

    @Test
    public void testSamplingGeometry() {
        assertThat(observation, is(notNullValue()));
        assertThat(observation.isSetSpatialFilteringProfileParameter(), is(true));
        assertThat(observation.getSpatialFilteringProfileParameter().getValue(), is(notNullValue()));
        assertThat(observation.getSpatialFilteringProfileParameter().getValue().getValue(),
                instanceOf(Geometry.class));
        assertThat(observation.getSpatialFilteringProfileParameter().getValue().getValue().getCoordinate().x,
                is(52.9));
        assertThat(observation.getSpatialFilteringProfileParameter().getValue().getValue().getCoordinate().y,
                is(7.52));
    }
}
