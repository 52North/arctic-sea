/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import org.n52.iceland.convert.RequestResponseModifierKey;
import org.n52.shetland.ogc.ows.service.OwsServiceResponse;
import org.n52.shetland.ogc.ows.service.OwsServiceRequest;

public class RequestResponseModifierKeyTypeTest {
    private static final String service = "SOS";
    private static final String version = "2.0.0";
    private final OwsServiceRequest request = new OwsServiceRequest() {};
    private final OwsServiceResponse response = new OwsServiceResponse() {};

    @Rule
    public final ErrorCollector errors = new ErrorCollector();

    @Test
    public void testHashCode() {
        errors.checkThat(new RequestResponseModifierKey(service, version, request).hashCode(), is(equalTo(new RequestResponseModifierKey(service, version, request).hashCode())));
        errors.checkThat(new RequestResponseModifierKey(service, version, request, response).hashCode(), is(equalTo(new RequestResponseModifierKey(service, version, request, response).hashCode())));
        errors.checkThat(new RequestResponseModifierKey(service, version, request).hashCode(), is(equalTo(new RequestResponseModifierKey(service, version, getModifiedRequest()).hashCode())));
        errors.checkThat(new RequestResponseModifierKey(service, version, request, response).hashCode(), is(equalTo(new RequestResponseModifierKey(service, version, getModifiedRequest(), getModifiedResponse()).hashCode())));

    }

    @Test
    @Ignore("sounds like this would offend the equals contract...")
    public void testEquals() {
        errors.checkThat(new RequestResponseModifierKey(service, version, request), is(equalTo(new RequestResponseModifierKey(service, version, request))));
        errors.checkThat(new RequestResponseModifierKey(service, version, request, response), is(equalTo(new RequestResponseModifierKey(service, version, request, response))));
        // for production
        errors.checkThat(new RequestResponseModifierKey(service, version, request, response), is(equalTo(new RequestResponseModifierKey(service, version, request))));
        errors.checkThat(new RequestResponseModifierKey(service, version, request, response), is(equalTo(new RequestResponseModifierKey(service, version, request, response))));
    }

    private OwsServiceRequest getModifiedRequest() {
        OwsServiceRequest request = new OwsServiceRequest() {};
        request.setService(service).setVersion(version);
        return request;
    }

    private OwsServiceResponse getModifiedResponse() {
        OwsServiceResponse response = new OwsServiceResponse() {};
        response.setService(service).setVersion(version);
        return response;
    }

}
