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
package org.n52.svalbard.encode.json;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.n52.svalbard.coding.json.JSONConstants.EXCEPTIONS;
import static org.n52.svalbard.coding.json.JSONConstants.LOCATOR;
import static org.n52.svalbard.coding.json.JSONConstants.TEXT;
import static org.n52.svalbard.coding.json.JSONConstants.VERSION;
import static org.n52.svalbard.coding.json.matchers.Does.does;
import static org.n52.svalbard.coding.json.matchers.JSONMatchers.arrayOfLength;
import static org.n52.svalbard.coding.json.matchers.JSONMatchers.exist;
import static org.n52.svalbard.coding.json.matchers.JSONMatchers.isObject;
import static org.n52.svalbard.coding.json.matchers.ValidationMatchers.instanceOf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.n52.svalbard.coding.json.SchemaConstants;
import org.n52.svalbard.encode.exception.EncoderResponseUnsupportedException;
import org.n52.svalbard.encode.json.JSONEncodingException;
import org.n52.svalbard.encode.json.OwsExceptionReportEncoder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class OwsExceptionReportEncoderTest {
    private OwsExceptionReportEncoder enc;

    @BeforeEach
    public void setUp() {
        enc = new OwsExceptionReportEncoder();
    }

    @Test
    public void testExceptionWithoutCause()
            throws JSONEncodingException {
        final EncoderResponseUnsupportedException owse = new EncoderResponseUnsupportedException();
        owse.setVersion("2.0.0");
        final JsonNode json = enc.encodeJSON(owse);
        assertThat(json, is(notNullValue()));
        final String message = "The encoder response is not supported!";
        assertThat(json, is(instanceOf(SchemaConstants.Common.EXCEPTION_REPORT)));
        assertThat(json.path(VERSION).asText(), is(equalTo("2.0.0")));
        assertThat(json.path(EXCEPTIONS), is(arrayOfLength(1)));
        assertThat(json.path(EXCEPTIONS).path(0), isObject());
        assertThat(json.path(EXCEPTIONS).path(0).path(LOCATOR), does(not(exist())));
        assertThat(json.path(EXCEPTIONS).path(0).path(TEXT).asText(), is(equalTo(message)));
    }
}
