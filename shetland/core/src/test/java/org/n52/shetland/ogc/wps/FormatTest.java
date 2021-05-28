/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.wps;

import org.n52.shetland.ogc.wps.Format;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class FormatTest {
    @Test
    public void test() {
        assertThat(new Format("text/xml").isCompatible(new Format("application/xml")), is(true));
        assertThat(new Format("text/*").isCompatible(new Format("text/plain")), is(true));
        assertThat(new Format("text/xml", StandardCharsets.UTF_8).isCompatible(new Format("text/xml")), is(false));
        assertThat(new Format("text/xml", StandardCharsets.UTF_8).isCompatible(new Format("text/xml", StandardCharsets.UTF_8)), is(true));
        assertThat(new Format("text/xml", StandardCharsets.ISO_8859_1).isCompatible(new Format("text/xml", StandardCharsets.UTF_8)), is(false));
        assertThat(new Format("text/xml").isCompatible(new Format("text/xml", StandardCharsets.UTF_8)), is(true));
        assertThat(new Format("text/xml").isCompatible(new Format("text/xml", Format.BASE64_ENCODING)), is(false));
        assertThat(new Format("text/*").isCompatible(new Format("text/plain")), is(true));
        assertThat(new Format("*/*").isCompatible(new Format("text/*")), is(true));
        assertThat(new Format("*/*").isCompatible(new Format("*/*")), is(true));
    }
}
