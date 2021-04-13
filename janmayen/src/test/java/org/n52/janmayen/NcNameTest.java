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
package org.n52.janmayen;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class NcNameTest {

    @Test
    public void testFixNcName() {
        String noFixString = "this_one_needs_no_fixing";
        String needsFixString = "1string&_needs_fixing";
        String fixedString = "_string__needs_fixing";
        assertEquals(noFixString, NcName.makeValid(noFixString));
        assertEquals(fixedString, NcName.makeValid(needsFixString));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionOnEmptyStringInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            NcName.makeValid("");
        });
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionOnNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            NcName.makeValid(null);
        });
    }

    @Test
    public void testHyphen() {
        String test = "test-nc-name";
        assertEquals(test, NcName.makeValid(test));
    }
}
