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
package org.n52.shetland.ogc.swe.simpleType;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SosSweCategoryTest {

    @Test
    public void should_return_false_for_all_isSetMethods_is_nothing_is_set() {
        final SweCategory category = new SweCategory();

        assertThat(category.isSetCodeSpace(), is(FALSE));
        assertThat(category.isSetDefinition(), is(FALSE));
        assertThat(category.isSetDescription(), is(FALSE));
        assertThat(category.isSetIdentifier(), is(FALSE));
        assertThat(category.isSetLabel(), is(FALSE));
        assertThat(category.isSetQuality(), is(FALSE));
        assertThat(category.isSetUom(), is(FALSE));
        assertThat(category.isSetValue(), is(FALSE));
        assertThat(category.isSetXml(), is(FALSE));
    }

    @Test
    public void should_return_true_afterSetting_CodeSpace() {
        final SweCategory category = new SweCategory();
        final String codeSpace = "test-code-space";
        category.setCodeSpace(codeSpace);

        assertThat(category.isSetCodeSpace(), is(TRUE));
        assertThat(category.getCodeSpace(), is(codeSpace));
    }
}
