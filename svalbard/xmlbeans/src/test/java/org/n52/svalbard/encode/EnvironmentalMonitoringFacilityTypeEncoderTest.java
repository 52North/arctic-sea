/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import java.util.Arrays;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.n52.janmayen.Producer;
import org.n52.svalbard.encode.exception.EncodingException;

import eu.europa.ec.inspire.schemas.ef.x40.EnvironmentalMonitoringFacilityType;

public class EnvironmentalMonitoringFacilityTypeEncoderTest
        extends AbstractEnvironmentalMonitoringFacilityEncoderTest {

    private static EnvironmentalMonitoringFaciltityTypeEncoder instance;

    @BeforeAll
    public static void initInstance() {
        instance = new EnvironmentalMonitoringFaciltityTypeEncoder();
        Producer<XmlOptions> options = () -> new XmlOptions();
        instance.setXmlOptions(options);

        InspireXmlEncoder inspireXmlEncoder = new InspireXmlEncoder();
        IdentifierPropertyTypeEncoder identifierPropertyTypeEncoder = new IdentifierPropertyTypeEncoder();
        GmlEncoderv321 gmlEncoderv321 = new GmlEncoderv321();

        EncoderRepository encoderRepository = new EncoderRepository();
        encoderRepository.setEncoders(
                Arrays.asList(instance, inspireXmlEncoder, identifierPropertyTypeEncoder, gmlEncoderv321));
        encoderRepository.init();

        instance.setEncoderRepository(encoderRepository);
        instance.setXmlOptions(options);
        inspireXmlEncoder.setEncoderRepository(encoderRepository);
        inspireXmlEncoder.setXmlOptions(options);
        identifierPropertyTypeEncoder.setEncoderRepository(encoderRepository);
        identifierPropertyTypeEncoder.setXmlOptions(options);
        gmlEncoderv321.setEncoderRepository(encoderRepository);
        gmlEncoderv321.setXmlOptions(options);
    }

    @Test
    public void encodeEnvironmentalMonitoringFacility() throws EncodingException {
        XmlObject xmlObject = instance.encode(getEnvironmentalMonitoringFacility());
        Assertions.assertEquals(xmlObject.validate(), true);
        Assertions.assertTrue(xmlObject instanceof EnvironmentalMonitoringFacilityType);
    }

}
