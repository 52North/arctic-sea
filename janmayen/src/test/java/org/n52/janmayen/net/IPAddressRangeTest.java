/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

/**
 * @since 1.0.0
 *
 */
public class IPAddressRangeTest {

    @Test
    public void should_return_true_for_ip_addresses_in_cidr_range() {
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.55.105"), is(true));
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.4.240"), is(true));
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.1.1"), is(true));
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.2.16"), is(true));
        assertThat(isAddressInRange("192.168.0.0/16", "192.168.255.255"), is(true));
    }

    @Test
    public void should_return_true_for_ip_addresses_in_subnet_range() {
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.55.105"), is(true));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.4.240"), is(true));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.1.1"), is(true));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.2.16"), is(true));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.168.255.255"), is(true));
    }

    @Test
    public void should_return_false_for_ip_addresses_outside_of_cidr_range() {
        assertThat(isAddressInRange("192.168.0.0/16", "10.1.16.100"), is(false));
        assertThat(isAddressInRange("192.168.0.0/16", "1.1.1.1"), is(false));
        assertThat(isAddressInRange("192.168.0.0/16", "255.255.255.255"), is(false));
        assertThat(isAddressInRange("192.168.0.0/16", "192.167.1.1"), is(false));
        assertThat(isAddressInRange("192.168.0.0/16", "192.169.1.1"), is(false));
    }

    @Test
    public void should_return_false_for_ip_addresses_outside_of_subnet_range() {
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "10.1.16.100"), is(false));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "1.1.1.1"), is(false));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "255.255.255.255"), is(false));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.167.1.1"), is(false));
        assertThat(isAddressInRange("192.168.0.0/255.255.0.0", "192.169.1.1"), is(false));
    }

    @Test
    public void should_return_true_for_single_ip_cidr_range() {
        assertThat(isAddressInRange("192.168.15.9/32", "192.168.15.9"), is(true));
    }

    @Test
    public void should_return_true_for_single_ip_subnet_range() {
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "192.168.15.9"), is(true));
    }

    @Test
    public void should_return_false_for_ips_outside_of_single_ip_cidr_range() {
        assertThat(isAddressInRange("192.168.15.9/32", "0.0.0.0"), is(false));
        assertThat(isAddressInRange("192.168.15.9/32", "192.168.15.8"), is(false));
        assertThat(isAddressInRange("192.168.15.9/32", "192.168.15.10"), is(false));
        assertThat(isAddressInRange("192.168.15.9/32", "192.168.14.9"), is(false));
    }

    @Test
    public void should_return_false_for_ips_outside_of_single_ip_subnet_range() {
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "0.0.0.0"), is(false));
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "192.168.15.8"), is(false));
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "192.168.15.10"), is(false));
        assertThat(isAddressInRange("192.168.15.9/255.255.255.255", "192.168.14.9"), is(false));
    }

    @Test
    public void should_return_true_for_valid_ip() {
        assertThat(isValidAddress("192.168.1.1"), is(true));
        assertThat(isValidAddress("127.0.0.1"), is(true));
    }

    @Test
    public void should_return_false_for_invalid_ip() {
        assertThat(isValidAddress("192.168.1.256"), is(false));
        assertThat(isValidAddress("192.168.1.1.9"), is(false));
        assertThat(isValidAddress("192.168.1"), is(false));
        assertThat(isValidAddress("192.168.01"), is(false));
        assertThat(isValidAddress("i am not an ip"), is(false));
    }

    @Test
    public void should_return_true_for_valid_cidr_addresses() {
        assertThat(isValidAddressRange("192.168.1.1/32"), is(true));
        assertThat(isValidAddressRange("127.0.0.1/32"), is(true));
        assertThat(isValidAddressRange("192.168.1.1/10"), is(true));
        assertThat(isValidAddressRange("1.1.1.1/0"), is(true));
    }

    @Test
    public void should_return_true_for_valid_subnet_addresses() {
        assertThat(isValidAddressRange("192.168.1.1/255.255.255.0"), is(true));
        assertThat(isValidAddressRange("127.0.0.1/255.255.0.0"), is(true));
        assertThat(isValidAddressRange("192.168.1.1/255.0.0.0"), is(true));
        assertThat(isValidAddressRange("1.1.1.1/0.0.0.0"), is(true));
    }

    @Test
    public void should_return_false_for_invalid_subnet_addresses() {
        assertThat(isValidAddressRange("192.168.1.256/255.255"), is(false));
        assertThat(isValidAddressRange("192.168.1.1.9/255.255.255.0.1"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/255.a.255.0"), is(false));
    }

    @Test
    public void should_return_false_for_invalid_cidr_addresses() {
        assertThat(isValidAddressRange("192.168.1.256/32"), is(false));
        assertThat(isValidAddressRange("192.168.1.1.9/32"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/-1"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/33"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/a"), is(false));
        assertThat(isValidAddressRange("192.168.1.01/a"), is(false));
        assertThat(isValidAddressRange("192.168.1.1/"), is(false));
        assertThat(isValidAddressRange("192.168.1.a/32"), is(false));
    }

    @Test
    public void should_throw_illegal_argument_exception() {
        assertThrows(IllegalArgumentException.class, () -> {
            isAddressInRange("192.168.0.0/a", "192.168.55.105");
        });

    }

    @Test
    public void should_throw_illegal_argument_exception2() {
        assertThrows(IllegalArgumentException.class, () -> {
            isAddressInRange("192.168.0.0/16", "192.168.55.a");
        });
    }

    @Test
    public void testGetPrefixForMaskIPv4() {
        testGetPrefixForMask("255.255.255.255", 32);
        testGetPrefixForMask("255.255.255.254", 31);
        testGetPrefixForMask("255.255.255.252", 30);
        testGetPrefixForMask("255.255.255.248", 29);
        testGetPrefixForMask("255.255.255.240", 28);
        testGetPrefixForMask("255.255.255.224", 27);
        testGetPrefixForMask("255.255.255.192", 26);
        testGetPrefixForMask("255.255.255.128", 25);
        testGetPrefixForMask("255.255.255.0", 24);
        testGetPrefixForMask("255.255.254.0", 23);
        testGetPrefixForMask("255.255.252.0", 22);
        testGetPrefixForMask("255.255.248.0", 21);
        testGetPrefixForMask("255.255.240.0", 20);
        testGetPrefixForMask("255.255.224.0", 19);
        testGetPrefixForMask("255.255.192.0", 18);
        testGetPrefixForMask("255.255.128.0", 17);
        testGetPrefixForMask("255.255.0.0", 16);
        testGetPrefixForMask("255.254.0.0", 15);
        testGetPrefixForMask("255.252.0.0", 14);
        testGetPrefixForMask("255.248.0.0", 13);
        testGetPrefixForMask("255.240.0.0", 12);
        testGetPrefixForMask("255.224.0.0", 11);
        testGetPrefixForMask("255.192.0.0", 10);
        testGetPrefixForMask("255.128.0.0", 9);
        testGetPrefixForMask("255.0.0.0", 8);
        testGetPrefixForMask("254.0.0.0", 7);
        testGetPrefixForMask("252.0.0.0", 6);
        testGetPrefixForMask("248.0.0.0", 5);
        testGetPrefixForMask("240.0.0.0", 4);
        testGetPrefixForMask("224.0.0.0", 3);
        testGetPrefixForMask("192.0.0.0", 2);
        testGetPrefixForMask("128.0.0.0", 1);
        testGetPrefixForMask("0.0.0.0", 0);
    }

    @Test
    public void testGetPrefixForMaskIPv6() {
        testGetPrefixForMask("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", 128);
        testGetPrefixForMask("ffff:ffff:ffff:ffff:ffff:ffff:ffff::", 112);
        testGetPrefixForMask("ffff:ffff:ffff:ffff:ffff:ffff::", 96);
        testGetPrefixForMask("ffff:ffff:ffff:ffff:ffff::", 80);
        testGetPrefixForMask("ffff:ffff:ffff:ffff::", 64);
        testGetPrefixForMask("ffff:ffff:ffff::", 48);
        testGetPrefixForMask("ffff:ffff::", 32);
        testGetPrefixForMask("ffff::", 16);
        testGetPrefixForMask("::", 0);
    }

    @Test
    public void testGetMaskForPrefixIPv6() {
        testGetMaskForPrefix(128, "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        testGetMaskForPrefix(112, "ffff:ffff:ffff:ffff:ffff:ffff:ffff::");
        testGetMaskForPrefix(96, "ffff:ffff:ffff:ffff:ffff:ffff::");
        testGetMaskForPrefix(80, "ffff:ffff:ffff:ffff:ffff::");
        testGetMaskForPrefix(64, "ffff:ffff:ffff:ffff::");
        testGetMaskForPrefix(48, "ffff:ffff:ffff::");
        testGetMaskForPrefix(32, "ffff:ffff::");
        testGetMaskForPrefix(16, "ffff::");
        testGetMaskForPrefix(0, "::");
    }

    @Test
    public void testGetMaskForPrefixIPv4() {
        testGetMaskForPrefix(32, "255.255.255.255");
        testGetMaskForPrefix(31, "255.255.255.254");
        testGetMaskForPrefix(30, "255.255.255.252");
        testGetMaskForPrefix(29, "255.255.255.248");
        testGetMaskForPrefix(28, "255.255.255.240");
        testGetMaskForPrefix(27, "255.255.255.224");
        testGetMaskForPrefix(26, "255.255.255.192");
        testGetMaskForPrefix(25, "255.255.255.128");
        testGetMaskForPrefix(24, "255.255.255.0");
        testGetMaskForPrefix(23, "255.255.254.0");
        testGetMaskForPrefix(22, "255.255.252.0");
        testGetMaskForPrefix(21, "255.255.248.0");
        testGetMaskForPrefix(20, "255.255.240.0");
        testGetMaskForPrefix(19, "255.255.224.0");
        testGetMaskForPrefix(18, "255.255.192.0");
        testGetMaskForPrefix(17, "255.255.128.0");
        testGetMaskForPrefix(16, "255.255.0.0");
        testGetMaskForPrefix(15, "255.254.0.0");
        testGetMaskForPrefix(14, "255.252.0.0");
        testGetMaskForPrefix(13, "255.248.0.0");
        testGetMaskForPrefix(12, "255.240.0.0");
        testGetMaskForPrefix(11, "255.224.0.0");
        testGetMaskForPrefix(10, "255.192.0.0");
        testGetMaskForPrefix(9, "255.128.0.0");
        testGetMaskForPrefix(8, "255.0.0.0");
        testGetMaskForPrefix(7, "254.0.0.0");
        testGetMaskForPrefix(6, "252.0.0.0");
        testGetMaskForPrefix(5, "248.0.0.0");
        testGetMaskForPrefix(4, "240.0.0.0");
        testGetMaskForPrefix(3, "224.0.0.0");
        testGetMaskForPrefix(2, "192.0.0.0");
        testGetMaskForPrefix(1, "128.0.0.0");
        testGetMaskForPrefix(0, "0.0.0.0");
    }

    private boolean isAddressInRange(String range, String address) {
        return new IPAddressRange(range).contains(new IPAddress(address));
    }

    private boolean isValidAddress(String address) {
        try {
            new IPAddress(address);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;

    }

    private boolean isValidAddressRange(String address) {
        try {
            new IPAddressRange(address);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private void testGetPrefixForMask(String address, int subnet) {
        assertThat(IPAddressRange.getPrefixForMask(new IPAddress(address)), is(subnet));
    }


    private void testGetMaskForPrefix(int subnet, String address) {
        IPAddress ipAddress = new IPAddress(address);
        assertThat(IPAddressRange.getMaskForPrefix(subnet, ipAddress.getBitSize()), is(ipAddress));
    }
}
