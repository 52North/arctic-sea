/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.request;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Csaba Lestar
 */
public class RequestContextTest {

    @Test
    public void ip4mappedip6RemoteAddress() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRemoteAddr("::ffff:217.173.34.182");
        RequestContext fromRequest = RequestContext.fromRequest(mockRequest);
        Assert.assertEquals("217.173.34.182", fromRequest.getIPAddress().get().toString());

        mockRequest.setRemoteAddr("0:0:0:0:0:ffff:217.173.34.182");
        fromRequest = RequestContext.fromRequest(mockRequest);
        Assert.assertEquals("217.173.34.182", fromRequest.getIPAddress().get().toString());
    }

    @Test
    public void ip4CompactIpv6Address() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRemoteAddr("::101.45.75.219");
        RequestContext fromRequest = RequestContext.fromRequest(mockRequest);
        Assert.assertEquals("101.45.75.219", fromRequest.getIPAddress().get().toString());

        mockRequest.setRemoteAddr("0:0:0:0:0:0:101.45.75.219");
        fromRequest = RequestContext.fromRequest(mockRequest);

        Assert.assertEquals("101.45.75.219", fromRequest.getIPAddress().get().toString());
    }

    @Test
    public void ipv6LocalhostAddress() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRemoteAddr("::1");
        RequestContext fromRequest = RequestContext.fromRequest(mockRequest);
        Assert.assertEquals("127.0.0.1", fromRequest.getIPAddress().get().toString());
    }

    @Test
    public void ip6to4Addresses() {
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.setRemoteAddr("2002:836b:1714::836b:1714");
        RequestContext fromRequest = RequestContext.fromRequest(mockRequest);
        Assert.assertEquals("131.107.23.20", fromRequest.getIPAddress().get().toString());
    }

}
