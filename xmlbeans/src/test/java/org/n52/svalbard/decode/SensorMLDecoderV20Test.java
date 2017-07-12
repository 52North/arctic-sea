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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Ignore;
import org.junit.Test;
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
@Ignore
public class SensorMLDecoderV20Test {

    @Test
    public void shouldDecodeDataInterface() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        SmlDataInterface parsedDataInterface = new SensorMLDecoderV20().parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface, is(notNullValue()));
    }

    @Test
    public void shouldDecodeDataInterfaceData() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        xbDataInterface.addNewData();
        SmlDataInterface parsedDataInterface = new SensorMLDecoderV20().parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface.getData(), is(notNullValue()));
    }

    @Test
    public void shouldDecodeDataInterfaceInterfaceParameters() throws DecodingException {
        DataInterfaceType xbDataInterface = DataInterfaceType.Factory.newInstance();
        DataRecordPropertyType xbInterfaceParameters = xbDataInterface.addNewInterfaceParameters();
        Field field = xbInterfaceParameters.addNewDataRecord().addNewField();
        field.setName("test-field-name");
        SmlDataInterface parsedDataInterface = new SensorMLDecoderV20().parseDataInterfaceType(xbDataInterface);
        assertThat(parsedDataInterface.isSetInterfaceParameters(),is(true));
        assertThat(parsedDataInterface.getInterfaceParameters(), CoreMatchers.isA(SweDataRecord.class));
    }

}
