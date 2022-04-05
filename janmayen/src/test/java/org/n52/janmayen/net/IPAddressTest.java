/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @author Christian Autermann
 */
public class IPAddressTest {

    @Test
    public void testCompareByteArrayLittleEndian() {
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] {}, new byte[] {}), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 0 }, new byte[] { 0 }), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1 }, new byte[] { 1 }), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 2 }, new byte[] { 2 }), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 3 }, new byte[] { 3 }), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 0 }, new byte[] { (byte) (255 & 0xff) }), lessThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1 }, new byte[] { (byte) (254 & 0xff) }), lessThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 2 }, new byte[] { (byte) (253 & 0xff) }), lessThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 3 }, new byte[] { (byte) (252 & 0xff) }), lessThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1 }, new byte[] {}), greaterThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1 }, new byte[] { 0 }), greaterThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1 }, new byte[] {}), greaterThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1 }, new byte[] { 0, 0 }), greaterThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1 }, new byte[] { 0, 1 }), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1, 1 }, new byte[] { 0, 1 }), greaterThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1, 1 }, new byte[] { 1, 1 }), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1, 1, 0  }, new byte[] { 1, 1 }), greaterThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1, 1, 0  }, new byte[] { 1, 1, 0  }), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1, 1, 1  }, new byte[] { 1, 1, 0  }), greaterThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1, 1, 0  }, new byte[] { 1, 1, 1  }), lessThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 1, 1, 1  }, new byte[] { 1, 1, 1  }), is(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] {}, new byte[] { 1 }), lessThan(0));
        assertThat(IPAddress.compareLittleEndianUnsignedByte(new byte[] { 0 }, new byte[] { 1 }), lessThan(0));
    }
}
