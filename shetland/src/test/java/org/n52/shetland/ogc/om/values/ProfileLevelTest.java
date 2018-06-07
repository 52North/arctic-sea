/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.om.values;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Before;
import org.junit.Test;
import org.n52.shetland.ogc.swe.SweDataRecord;

public class ProfileLevelTest {

    private ProfileLevel level;

    @Before
    public void setUp() {
        level = new ProfileLevel();
        TextValue textValue1 = new TextValue("text 1");
        textValue1.setDefinition("definition");
        textValue1.setLabel("label");
        textValue1.setDescription("description");
        TextValue textValue2 = new TextValue("text 2");
        textValue2.setDefinition("definition");
        textValue2.setLabel("label");
        textValue2.setDescription("description");
        level.addValue(textValue1);
        level.addValue(textValue2);
    }

    @Test
    public void test_relabel_field_names() {
        SweDataRecord dataRecord = level.valueAsDataRecord();
        assertThat(dataRecord.getFields().size(), is(2));
        assertThat(dataRecord.getFields().get(0).getName().getValue(), equalTo("label_1"));
        assertThat(dataRecord.getFields().get(1).getName().getValue(), equalTo("label_2"));
    }

    @Test
    public void test_field_name() {
        level.getValue().remove(1);
        SweDataRecord dataRecord = level.valueAsDataRecord();
        assertThat(dataRecord.getFields().size(), is(1));
        assertThat(dataRecord.getFields().get(0).getName().getValue(), equalTo("label"));
    }

    @Test
    public void test_not_relabel_field_names() {
        ((TextValue) level.getValue().get(1)).setLabel("label_test");
        SweDataRecord dataRecord = level.valueAsDataRecord();
        assertThat(dataRecord.getFields().size(), is(2));
        assertThat(dataRecord.getFields().get(0).getName().getValue(), equalTo("label"));
        assertThat(dataRecord.getFields().get(1).getName().getValue(), equalTo("label_test"));
    }
}
