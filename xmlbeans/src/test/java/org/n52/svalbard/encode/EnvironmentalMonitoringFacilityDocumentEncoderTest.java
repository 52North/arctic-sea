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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import org.apache.xmlbeans.XmlObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.n52.svalbard.encode.exception.EncodingException;

import eu.europa.ec.inspire.schemas.ef.x40.EnvironmentalMonitoringFacilityDocument;

public class EnvironmentalMonitoringFacilityDocumentEncoderTest
        extends AbstractEnvironmentalMonitoringFacilityEncoderTest {
//
//    @Rule
//    public final ErrorCollector errors = new ErrorCollector();
//
//    private final EnvironmentalMonitoringFaciltityDocumentEncoder docEncoder =
//            new EnvironmentalMonitoringFaciltityDocumentEncoder();
//
//    @Test
//    public void encodeEnvironmentalMonitoringFacility() throws EncodingException {
//        XmlObject xmlObject = docEncoder.encode(getEnvironmentalMonitoringFacility());
//        errors.checkThat(xmlObject.validate(), is(true));
//        errors.checkThat(xmlObject, instanceOf(EnvironmentalMonitoringFacilityDocument.class));
//    }
}
