/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import org.n52.shetland.ogc.sensorML.elements.SmlClassifier;

public class SmlClassifierTest {

    @Test public void
    shouldReturnTrueIfCodeSpaceIsSetAndNotEmpty()
    {
        final String codeSpace = "test-codespace";
        final SmlClassifier smlClassifier = new SmlClassifier("name", "definition", codeSpace, "value");
        assertThat(smlClassifier.isSetCodeSpace(), is(true));
        assertThat(smlClassifier.getCodeSpace(), is(codeSpace));
    }

    @Test public void
    shouldReturnFalseIfCodeSpaceIsEmptyOrNotSet()
    {
        final String codeSpace = null;
        final SmlClassifier smlClassifier = new SmlClassifier("name", "definition", codeSpace, "value");
        assertThat(smlClassifier.isSetCodeSpace(), is(false));

        smlClassifier.setCodeSpace("");
        assertThat(smlClassifier.isSetCodeSpace(), is(false));
    }

    @Test public void
    shouldReturnTrueIfDefinitionIsSetAndNotEmpty()
    {
        final String definition = "test-definition";
        final SmlClassifier smlClassifier = new SmlClassifier(null, definition, null, null);
        assertThat(smlClassifier.isSetDefinition(), is(true));
        assertThat(smlClassifier.getDefinition(), is(definition));
    }

    @Test public void
    shouldReturnFalseIfDefinitionIsEmptyOrNotSet()
    {
        final String definition = null;
        final SmlClassifier smlClassifier = new SmlClassifier("name", definition, "codeSpace", "value");
        assertThat(smlClassifier.isSetDefinition(), is(false));

        smlClassifier.setDefinition("");
        assertThat(smlClassifier.isSetDefinition(), is(false));
    }
}
