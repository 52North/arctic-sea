/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.converter;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.n52.iceland.convert.RequestResponseModifierKey;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;

public class RequestResponseModifierKeyTypeTest {
    private static final String SERVICE = "SOS";
    private static final String VERSION = "2.0.0";
    private final OwsServiceRequest request = new RequestImpl();
    private final OwsServiceResponse response = new ResponseImpl();

    @Test
    public void testHashCode() {
        assertThat(new RequestResponseModifierKey(SERVICE, VERSION, request).hashCode(), is(equalTo(new RequestResponseModifierKey(SERVICE, VERSION, request).hashCode())));
        assertThat(new RequestResponseModifierKey(SERVICE, VERSION, request, response).hashCode(), is(equalTo(new RequestResponseModifierKey(SERVICE, VERSION, request, response).hashCode())));
        assertThat(new RequestResponseModifierKey(SERVICE, VERSION, request).hashCode(), is(equalTo(new RequestResponseModifierKey(SERVICE, VERSION, getModifiedRequest()).hashCode())));
        assertThat(new RequestResponseModifierKey(SERVICE, VERSION, request, response).hashCode(), is(equalTo(new RequestResponseModifierKey(SERVICE, VERSION, getModifiedRequest(), getModifiedResponse()).hashCode())));

    }

    @Test
    @Disabled("sounds like this would offend the equals contract...")
    public void testEquals() {
        assertThat(new RequestResponseModifierKey(SERVICE, VERSION, request),
                         is(equalTo(new RequestResponseModifierKey(SERVICE, VERSION, request))));
        assertThat(new RequestResponseModifierKey(SERVICE, VERSION, request, response),
                         is(equalTo(new RequestResponseModifierKey(SERVICE, VERSION, request, response))));
        // for production
        assertThat(new RequestResponseModifierKey(SERVICE, VERSION, request, response),
                         is(equalTo(new RequestResponseModifierKey(SERVICE, VERSION, request))));
        assertThat(new RequestResponseModifierKey(SERVICE, VERSION, request, response),
                         is(equalTo(new RequestResponseModifierKey(SERVICE, VERSION, request, response))));
    }

    private OwsServiceRequest getModifiedRequest() {
        OwsServiceRequest request = new RequestImpl();
        request.setService(SERVICE).setVersion(VERSION);
        return request;
    }

    private OwsServiceResponse getModifiedResponse() {
        OwsServiceResponse response = new ResponseImpl();
        response.setService(SERVICE).setVersion(VERSION);
        return response;
    }

    private static class RequestImpl extends OwsServiceRequest {};
    private static class ResponseImpl extends OwsServiceResponse {};

}
