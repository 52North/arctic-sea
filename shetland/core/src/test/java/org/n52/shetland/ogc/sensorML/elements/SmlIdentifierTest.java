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
package org.n52.shetland.ogc.sensorML.elements;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SmlIdentifierTest {

    @Test
    public void should_return_false_if_name_not_set_correct() {
        final SmlIdentifier smlIdentifier = new SmlIdentifier(null, "tmp", "tmp");
        final SmlIdentifier smlId2 = new SmlIdentifier("", "tmp", "tmp");

        assertThat(smlIdentifier.isSetName(), is(FALSE));
        assertThat(smlId2.isSetName(), is(FALSE));
    }

    @Test
    public void should_return_true_if_name_is_set() {
        final String name = "name";
        final SmlIdentifier identifier = new SmlIdentifier(name, null, null);

        assertThat(identifier.isSetName(), is(TRUE));
        assertThat(identifier.getName(), is(name));
    }

    @Test
    public void should_return_true_if_definition_is_set() {
        final String definition = "definition";
        final SmlIdentifier identifier = new SmlIdentifier(null, definition, null);

        assertThat(identifier.isSetDefinition(), is(TRUE));
        assertThat(identifier.getDefinition(), is(definition));
    }

    @Test
    public void should_return_true_if_value_is_set() {
        final String value = "value";
        final SmlIdentifier identifier = new SmlIdentifier(null, null, value);

        assertThat(identifier.isSetValue(), is(TRUE));
        assertThat(identifier.getValue(), is(value));
    }

    @Test
    public void should_return_false_if_value_not_set_correct() {
        final SmlIdentifier smlIdentifier = new SmlIdentifier("tmp", "tmp", null);
        final SmlIdentifier smlId2 = new SmlIdentifier("tmp", "tmp", "");

        assertThat(smlIdentifier.isSetValue(), is(FALSE));
        assertThat(smlId2.isSetValue(), is(FALSE));
    }

    @Test
    public void should_return_false_if_definition_not_set_correct() {
        final SmlIdentifier smlIdentifier = new SmlIdentifier("tmp", null, "tmp");
        final SmlIdentifier smlId2 = new SmlIdentifier("tmp", "", "tmp");

        assertThat(smlIdentifier.isSetDefinition(), is(FALSE));
        assertThat(smlId2.isSetDefinition(), is(FALSE));
    }
}
