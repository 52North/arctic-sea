/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.swe;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import org.n52.shetland.ogc.swe.simpleType.SweBoolean;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SweDataRecordTest {

    @Test
    public final void getFieldIndexByIdentifier_should_return_negativeValue_if_not_found_or_empty() throws Exception {
        assertThat(new SweDataRecord().getFieldIndexByIdentifier("test-identifier"), is(-1));
    }

    @Test
    public void getFieldIndexByIdentifier_should_not_fail_on_bad_input() {
        final SweDataRecord dataRecord = new SweDataRecord();
        dataRecord.addField(new SweField("identifier", new SweBoolean()));
        final SweBoolean b = new SweBoolean();
        b.setDefinition("test-element-definition");
        dataRecord.addField(new SweField("test-field-name", b));

        assertThat(dataRecord.getFieldIndexByIdentifier(null), is(-1));
        assertThat(dataRecord.getFieldIndexByIdentifier(""), is(-1));
    }

    @Test
    public void getFieldIndexByIdentifier_should_return_one() {
        final SweDataRecord dataRecord = new SweDataRecord();
        dataRecord.addField(new SweField("identifier", new SweBoolean()));
        final SweBoolean b = new SweBoolean();
        final String definition = "test-element-definition";
        b.setDefinition(definition);
        final String name = "test-field-name";
        dataRecord.addField(new SweField(name, b));

        assertThat(dataRecord.getFieldIndexByIdentifier(definition), is(1));
        assertThat(dataRecord.getFieldIndexByIdentifier(name), is(1));
    }

}
