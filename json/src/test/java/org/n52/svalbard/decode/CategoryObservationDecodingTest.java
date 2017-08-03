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
import org.n52.shetland.ogc.om.values.CategoryValue;
import org.n52.svalbard.ConfiguredSettingsManager;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.ObservationDecoder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class CategoryObservationDecodingTest {
    @ClassRule
    public static final ConfiguredSettingsManager csm = new ConfiguredSettingsManager();

    private static DateTime phenomenonTime;

    private static JsonNode json;

    private ObservationDecoder decoder;

    private OmObservation observation;

    @BeforeClass
    public static void beforeClass() {
        try {
            json = fromResource("/examples/category-observation.json");
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
        assertThat(type, is(equalTo(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION)));
        final ObservationValue<?> value = observation.getValue();
        assertThat(value, is(instanceOf(SingleObservationValue.class)));
        assertThat(value.getPhenomenonTime(), is(instanceOf(TimeInstant.class)));
        TimeInstant pt = (TimeInstant) value.getPhenomenonTime();
        assertThat(pt.getValue(), is(equalTo(phenomenonTime)));
        assertThat(value.getValue(), is(instanceOf(CategoryValue.class)));
        CategoryValue v = (CategoryValue) value.getValue();
        assertThat(v.getValue(), is(equalTo("Some Value")));
        assertThat(v.getUnit(), is(equalTo("http://52north.org/")));
    }
}
