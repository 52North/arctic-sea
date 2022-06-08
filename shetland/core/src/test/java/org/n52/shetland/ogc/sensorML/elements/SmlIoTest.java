/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;
import org.n52.shetland.ogc.swe.simpleType.SweBoolean;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 *
 * @since 1.0.0
 */
public class SmlIoTest {

    @Test
    public void should_return_false_if_ioValue_is_not_set() {
        final SmlIo smlIo = new SmlIo();
        assertThat(smlIo.isSetValue(), is(FALSE));
    }

    @Test
    public void should_return_true_if_ioValue_is_set() {
        final SweBoolean ioValue = new SweBoolean();
        final SmlIo smlIo = new SmlIo(ioValue);
        assertThat(smlIo.isSetValue(), is(TRUE));
    }

    @Test
    public void should_return_false_if_ioName_is_not_set() {
        final SmlIo smlIo = new SmlIo();
        assertThat(smlIo.isSetName(), is(FALSE));

        smlIo.setIoName("");
        assertThat(smlIo.isSetName(), is(FALSE));
    }

    @Test
    public void should_return_true_if_ioName_is_set() {
        final SmlIo smlIo = new SmlIo();
        final String inputName = "inputName";
        smlIo.setIoName(inputName);
        assertThat(smlIo.isSetName(), is(TRUE));
    }
}
