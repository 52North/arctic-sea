/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.sensorML.AbstractSensorML;
import org.n52.shetland.ogc.sensorML.v20.AggregateProcess;
import org.n52.shetland.ogc.sensorML.v20.SmlDataInterface;
import org.n52.shetland.ogc.swe.SweDataRecord;
import org.n52.svalbard.decode.exception.DecodingException;

import net.opengis.sensorml.x20.DataInterfaceType;
import net.opengis.swe.x20.DataRecordPropertyType;
import net.opengis.swe.x20.DataRecordType.Field;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SensorMLDecoderV20Test {

    private SensorMLDecoderV20 decoder;

    @BeforeEach
    public void setup() {
        DecoderRepository decoderRepository = new DecoderRepository();

        decoder = new SensorMLDecoderV20();
        decoder.setDecoderRepository(decoderRepository);
        decoder.setXmlOptions(XmlOptions::new);

        SweCommonDecoderV20 sweCommonDecoder = new SweCommonDecoderV20();
        sweCommonDecoder.setDecoderRepository(decoderRepository);
        sweCommonDecoder.setXmlOptions(XmlOptions::new);

        GmlDecoderv321 gmlDecoder = new GmlDecoderv321();
        gmlDecoder.setDecoderRepository(decoderRepository);
        gmlDecoder.setXmlOptions(XmlOptions::new);

        decoderRepository.setDecoders(Arrays.asList(decoder, sweCommonDecoder, gmlDecoder));
        decoderRepository.init();
    }

    @Test
    @Disabled("not yet implemented")
    public void bla() throws IOException, XmlException, DecodingException {
        try (InputStream stream = SensorMLDecoderV20Test.class.getResourceAsStream("/AggregateProcess-dwd.xml")) {
            XmlObject object = XmlObject.Factory.parse(stream);
            AbstractSensorML ssml = decoder.decode(object);
            assertThat(ssml, is(instanceOf(AggregateProcess.class)));
        }
    }

    @Test
    @Disabled("not yet implemented")
    public void shouldDecodeDataInterface() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        SmlDataInterface parsedDataInterface = getDecoder().parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface, is(notNullValue()));
    }

    @Test
    @Disabled("not yet implemented")
    public void shouldDecodeDataInterfaceData() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        xbDataInterface.addNewData();
        SmlDataInterface parsedDataInterface = getDecoder().parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface.getData(), is(notNullValue()));
    }

    @Test
    @Disabled("not yet implemented")
    public void shouldDecodeDataInterfaceInterfaceParameters() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        DataRecordPropertyType xbInterfaceParameters = xbDataInterface.addNewInterfaceParameters();
        Field field = xbInterfaceParameters.addNewDataRecord().addNewField();
        field.setName("test-field-name");
        SmlDataInterface parsedDataInterface = getDecoder().parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface.isSetInterfaceParameters(),is(true));
        assertThat(parsedDataInterface.getInterfaceParameters(), CoreMatchers.isA(SweDataRecord.class));
    }

    private static SensorMLDecoderV20 getDecoder() {
        DecoderRepository decoderRepository = new DecoderRepository();

        SensorMLDecoderV20 sensorMLDecoderV20 = new SensorMLDecoderV20();
        sensorMLDecoderV20.setDecoderRepository(decoderRepository);
        sensorMLDecoderV20.setXmlOptions(XmlOptions::new);

        SweCommonDecoderV20 sweCommonDecoderV20 = new SweCommonDecoderV20();
        sweCommonDecoderV20.setDecoderRepository(decoderRepository);
        sweCommonDecoderV20.setXmlOptions(XmlOptions::new);

        decoderRepository.setDecoders(Arrays.asList(sensorMLDecoderV20,
                                                    sweCommonDecoderV20));
        decoderRepository.init();
        return sensorMLDecoderV20;
    }

}