/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import org.n52.shetland.ogc.gml.CodeWithAuthority;
import org.n52.shetland.ogc.om.features.samplingFeatures.SamplingFeature;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;
import org.n52.shetland.ogc.sensorML.v20.PhysicalComponent;

public class OmObservationConstellationTest {

    private final String PROCEDURE_ID = "http://sensors.portdebarcelona.cat/def/weather/procedure";

    private final AbstractSensorML PROCEDURE = new PhysicalComponent().setIdentifier(PROCEDURE_ID);

    private final String OFFERING = "http://sensors.portdebarcelona.cat/def/weather/offerings#10m";

    private final String FEATURE_1 = "http://sensors.portdebarcelona.cat/def/weather/features#03";

    private final String FEATURE_2 = "http://sensors.portdebarcelona.cat/def/weather/features#P3";

    private final String OBSERVABLE_PROPERTY_1 = "http://sensors.portdebarcelona.cat/def/weather/properties#31N";

    private final String OBSERVABLE_PROPERTY_2 = "http://sensors.portdebarcelona.cat/def/weather/properties#30M";

    private OmObservationConstellation getFirstObservationConstellation() {
        return new OmObservationConstellation().setProcedure(PROCEDURE).addOffering(OFFERING)
                .setFeatureOfInterest(new SamplingFeature(new CodeWithAuthority(FEATURE_1)))
                .setObservableProperty(new OmObservableProperty(OBSERVABLE_PROPERTY_1));

    }

    private OmObservationConstellation getSecondObservationConstellation() {
        return new OmObservationConstellation().setProcedure(PROCEDURE).addOffering(OFFERING)
                .setFeatureOfInterest(new SamplingFeature(new CodeWithAuthority(FEATURE_2)))
                .setObservableProperty(new OmObservableProperty(OBSERVABLE_PROPERTY_2));
    }

    @Test
    public void shouldNotBeEqualHashCode() {
        assertThat(getFirstObservationConstellation().hashCode(), not(getSecondObservationConstellation().hashCode()));
    }

    @Test
    public void testChecheckObservationTypeForMerging() {
        OmObservationConstellation ooc = new OmObservationConstellation();
        ooc.setObservationType(OmConstants.OBS_TYPE_MEASUREMENT);
        assertThat(ooc.checkObservationTypeForMerging(), is(true));
        ooc.setObservationType(OmConstants.OBS_TYPE_CATEGORY_OBSERVATION);
        assertThat(ooc.checkObservationTypeForMerging(), is(true));
        ooc.setObservationType(OmConstants.OBS_TYPE_COUNT_OBSERVATION);
        assertThat(ooc.checkObservationTypeForMerging(), is(true));
        ooc.setObservationType(OmConstants.OBS_TYPE_GEOMETRY_OBSERVATION);
        assertThat(ooc.checkObservationTypeForMerging(), is(true));
        ooc.setObservationType(OmConstants.OBS_TYPE_TEXT_OBSERVATION);
        assertThat(ooc.checkObservationTypeForMerging(), is(true));
        ooc.setObservationType(OmConstants.OBS_TYPE_TRUTH_OBSERVATION);
        assertThat(ooc.checkObservationTypeForMerging(), is(true));
        ooc.setObservationType(OmConstants.OBS_TYPE_SWE_ARRAY_OBSERVATION);
        assertThat(ooc.checkObservationTypeForMerging(), is(false));
        ooc.setObservationType(OmConstants.OBS_TYPE_COMPLEX_OBSERVATION);
        assertThat(ooc.checkObservationTypeForMerging(), is(false));
        ooc.setObservationType(OmConstants.OBS_TYPE_OBSERVATION);
        assertThat(ooc.checkObservationTypeForMerging(), is(false));
        ooc.setObservationType(OmConstants.OBS_TYPE_UNKNOWN);
        assertThat(ooc.checkObservationTypeForMerging(), is(false));

    }

}
