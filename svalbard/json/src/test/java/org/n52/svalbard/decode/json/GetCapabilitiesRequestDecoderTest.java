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
package org.n52.svalbard.decode.json;

import static com.github.fge.jackson.JsonLoader.fromResource;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.n52.shetland.ogc.ows.service.GetCapabilitiesRequest;
import org.n52.shetland.ogc.sos.Sos1Constants;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.svalbard.ConfiguredSettingsManager;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.json.GetCapabilitiesRequestDecoder;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
@ExtendWith(ConfiguredSettingsManager.class)
public class GetCapabilitiesRequestDecoderTest {

    private static JsonNode json;

    private GetCapabilitiesRequestDecoder decoder;

    private GetCapabilitiesRequest request;

    @BeforeAll
    public static void beforeClass() {
        try {
            json = fromResource("/examples/sos/GetCapabilitiesRequest.json");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @BeforeEach
    public void before()
            throws DecodingException {
        this.decoder = new GetCapabilitiesRequestDecoder();
        this.request = decoder.decodeJSON(json, true);
    }

    @Test
    public void testService() {
        assertThat(request.getService(), is(SosConstants.SOS));
    }

    @Test
    public void testAcceptVersions() {
        assertThat(request.getAcceptVersions(), contains(Sos2Constants.SERVICEVERSION, Sos1Constants.SERVICEVERSION));
    }

    @Test
    public void testAcceptFormats() {
        assertThat(request.getAcceptFormats(), contains("application/json", "application/xml", "text/xml"));
    }

    @Test
    public void testSections() {
        assertThat(request.getSections(), contains("Contents"));
    }

    @Test
    public void testOperationName() {
        assertThat(request.getOperationName(), is(SosConstants.Operations.GetCapabilities.name()));
    }
}
