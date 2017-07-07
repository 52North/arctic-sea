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
package org.n52.svalbard.encode;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.n52.shetland.ogc.sensorML.AbstractProcess;
import org.n52.shetland.ogc.sensorML.System;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilities;
import org.n52.shetland.ogc.sensorML.elements.SmlCapabilitiesPredicates;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.simpleType.SweText;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class AbstractSensorMLEncoderTest {

    private TestAbstractSensorMLEncoder encoder = new TestAbstractSensorMLEncoder();

    private final String CAPABILITIES_NAME = "featuresOfInterest";

    private final String DEFINITION = "http://www.opengis.net/def/featureOfInterest/identifier";

    private final String FIELD_NAME = "featureOfInterestID";

    private final String FEATURE_ID = "http://www.52north.org/test/featureOfInterest/1";


//    @Test
//    public void testMergeFeatureCapabilities() {
//       AbstractProcess abstractProcess = getAbstractProcess();
//       encoder.mergeCapabilities(abstractProcess, CAPABILITIES_NAME, DEFINITION, FIELD_NAME, Sets.newHashSet(getSweText("blabla", FEATURE_ID)));
//       assertThat(abstractProcess.isSetCapabilities(), is(true));
//       Optional<SmlCapabilities> capabilities =
//               abstractProcess.findCapabilities(SmlCapabilitiesPredicates.name(CAPABILITIES_NAME));
//       assertThat(capabilities.isPresent(), is(true));
//       for (SweField field : capabilities.get().getDataRecord().getFields()) {
//           assertThat(FIELD_NAME.equalsIgnoreCase(field.getName().getValue()), is(true));
//           assertThat(DEFINITION.equalsIgnoreCase(((SweText)field.getElement()).getDefinition()), is(true));
//           assertThat(FEATURE_ID.equalsIgnoreCase(((SweText)field.getElement()).getValue()), is(true));
//       }
//    }
//
//    private AbstractProcess getAbstractProcess() {
//        System system = new System();
//        system.addCapabilities(getCapabilites());
//        return system;
//    }
//
//    private SmlCapabilities getCapabilites() {
//        SmlCapabilities caps = new SmlCapabilities(CAPABILITIES_NAME);
//        caps.setDataRecord(getDataRecord());
//        return caps;
//    }
//
//    private SweDataRecord getDataRecord() {
//        SweDataRecord record = new SweDataRecord();
//        SweField field = new SweField(FIELD_NAME, getSweText(FIELD_NAME, FEATURE_ID));
//        record.addField(field);
//        return record;
//    }
//
//    private SweText getSweText(String name, String value) {
//        SweText sweText = new SweText();
//        sweText.setValue(value);
//        sweText.setName(name);
//        return sweText;
//    }
}
