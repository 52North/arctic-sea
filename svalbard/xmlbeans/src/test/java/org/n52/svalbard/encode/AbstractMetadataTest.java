/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
import static org.junit.Assert.assertThat;

import java.util.LinkedList;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.ows.extension.Extension;
import org.n52.shetland.ogc.swe.SweDataArray;
import org.n52.shetland.ogc.swe.SweField;
import org.n52.shetland.ogc.swe.SweSimpleDataRecord;
import org.n52.shetland.ogc.swe.simpleType.SweCount;
import org.n52.shetland.ogc.swes.SwesExtension;
import org.n52.svalbard.util.SweHelper;

import net.opengis.swe.x20.DataArrayPropertyType;
import net.opengis.swe.x20.DataArrayType;
import net.opengis.swe.x20.DataRecordType;


public abstract class AbstractMetadataTest {

    protected void checkMetadataResponse(XmlObject[] extensionArray) throws XmlException {
        assertThat(extensionArray != null, is(true));
        assertThat(extensionArray.length, is(1));
        XmlObject parse = XmlObject.Factory.parse(extensionArray[0].xmlText());
        assertThat(parse, instanceOf(DataArrayPropertyType.class));
        DataArrayPropertyType dad = (DataArrayPropertyType) parse;
        assertThat(dad.getDataArray1(), instanceOf(DataArrayType.class));
        DataArrayType dat = dad.getDataArray1();
        assertThat(dat.getElementType().isSetAbstractDataComponent(), is (true));
        assertThat(dat.getElementType().getAbstractDataComponent(), instanceOf(DataRecordType.class));
    }


    protected Extension<SweDataArray> createExtension() {
        SweDataArray sweDataArray = new SweDataArray();
        sweDataArray.setElementCount(new SweCount().setValue(2));
        sweDataArray.setEncoding(new SweHelper().createTextEncoding(";", ",", "."));

        SweSimpleDataRecord dataRecord = new SweSimpleDataRecord();
        dataRecord.setDefinition("Components");
        dataRecord.addField(new SweField("test_id"));
        dataRecord.addField(new SweField("test_code"));
        dataRecord.addField(new SweField("test_desc"));
        sweDataArray.setElementType(dataRecord);

        LinkedList<List<String>> values = new LinkedList<List<String>>();
        List<String> blockOfTokens_1 = new LinkedList<>();
        blockOfTokens_1.add("1");
        blockOfTokens_1.add("code_1");
        blockOfTokens_1.add("desc_1");
        values.add(blockOfTokens_1);
        List<String> blockOfTokens_2 = new LinkedList<>();
        blockOfTokens_2.add("2");
        blockOfTokens_2.add("code_2");
        blockOfTokens_2.add("desc_2");
        values.add(blockOfTokens_2);

        sweDataArray.setValues(values);
        return new SwesExtension<SweDataArray>().setValue(sweDataArray).setIdentifier("test")
                .setDefinition("test");
    }
}
