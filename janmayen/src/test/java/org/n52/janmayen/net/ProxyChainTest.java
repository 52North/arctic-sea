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
package org.n52.janmayen.net;

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

public class ProxyChainTest {

    @Test
    public void shouldReturnNullOnInvalidIPv4() {
        isInvalid("192.168.52.123:");
        isInvalid("192.168.52.");
        isInvalid("192.168.52");
        isInvalid("192.168.");
        isInvalid("192.168");
        isInvalid("192.");
        isInvalid("192");
        isInvalid("");
        isInvalid("256.168.52.123");
        isInvalid("192.256.52.123");
        isInvalid("192.168.256.123");
        isInvalid("192.168.52.256");
    }

    @Test
    public void shouldReturnNullOnInvalidIPv6() {
        isInvalid("[2a02:2e0:40c:ffff::3]:");
        isInvalid("[2a02:2e0:40c:ffff::3");
        isInvalid("2a02:2e0:40c:ffff::3]");
    }

    @Test
    public void shouldHandleIPv4() {
        isValid("192.168.52.123", "192.168.52.123");
        isValid("192.168.52.123:50684", "192.168.52.123");
    }

    @Test
    public void shouldHandleIPv6() {
        isValid("[2a02:2e0:40c:ffff::3]:80", "2a02:2e0:40c:ffff:0:0:0:3");
        isValid("[2a02:2e0:40c:ffff::3]", "2a02:2e0:40c:ffff:0:0:0:3");
        isValid("2a02:2e0:40c:ffff::3", "2a02:2e0:40c:ffff:0:0:0:3");
        isValid("::192.168.52.123", "0:0:0:0:0:0:c0a8:347b");
        isValid("::ffff:192.168.52.123", "192.168.52.123");
        isValid("::ffff:0:192.168.52.123", "0:0:0:0:ffff:0:c0a8:347b");
        isValid("[::192.168.52.123]", "0:0:0:0:0:0:c0a8:347b");
        isValid("[::ffff:192.168.52.123]", "192.168.52.123");
        isValid("[::ffff:0:192.168.52.123]", "0:0:0:0:ffff:0:c0a8:347b");
        isValid("[::192.168.52.123]:80", "0:0:0:0:0:0:c0a8:347b");
        isValid("[::ffff:192.168.52.123]:80", "192.168.52.123");
        isValid("[::ffff:0:192.168.52.123]:80", "0:0:0:0:ffff:0:c0a8:347b");
    }

    private void isInvalid(String addr) {
        assertThat(ProxyChain.getIPAddress(addr), is(nullValue()));
    }

    private void isValid(String input, String expected) {
        assertThat(ProxyChain.getIPAddress(input).toString(), is(expected));
    }

}
