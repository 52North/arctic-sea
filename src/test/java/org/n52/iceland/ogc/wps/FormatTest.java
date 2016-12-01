/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.wps;

import org.n52.shetland.ogc.wps.Format;

import static org.hamcrest.Matchers.is;

import java.nio.charset.StandardCharsets;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class FormatTest {
    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void test() {
        errors.checkThat(new Format("text/xml").isCompatible(new Format("application/xml")), is(true));
        errors.checkThat(new Format("text/*").isCompatible(new Format("text/plain")), is(true));
        errors.checkThat(new Format("text/xml", StandardCharsets.UTF_8).isCompatible(new Format("text/xml")), is(false));
        errors.checkThat(new Format("text/xml", StandardCharsets.UTF_8).isCompatible(new Format("text/xml", StandardCharsets.UTF_8)), is(true));
        errors.checkThat(new Format("text/xml", StandardCharsets.ISO_8859_1).isCompatible(new Format("text/xml", StandardCharsets.UTF_8)), is(false));
        errors.checkThat(new Format("text/xml").isCompatible(new Format("text/xml", StandardCharsets.UTF_8)), is(true));
        errors.checkThat(new Format("text/xml").isCompatible(new Format("text/xml", Format.BASE64_ENCODING)), is(false));
        errors.checkThat(new Format("text/*").isCompatible(new Format("text/plain")), is(true));
        errors.checkThat(new Format("*/*").isCompatible(new Format("text/*")), is(true));
        errors.checkThat(new Format("*/*").isCompatible(new Format("*/*")), is(true));
    }
}
