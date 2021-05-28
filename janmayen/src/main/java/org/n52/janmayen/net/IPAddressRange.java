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
package org.n52.janmayen.net;

import java.util.Objects;
import java.util.function.Predicate;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Range;
import com.google.common.primitives.Ints;

/**
 * Representation of an IPv4 address range based on an address and a subnet mask.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 */
public class IPAddressRange implements Predicate<IPAddress>, com.google.common.base.Predicate<IPAddress> {
    private static final String NOT_VALID_ERR_MSG = "Not a valid range address!";
    private static final String RANGE_SEPARATOR = "/";
    private static final long MAX_UNSIGNED_LONG = 0xffffffffffffffffL;
    private static final int MAX_UNSIGNED_INT = 0xffffffff;
    private final IPAddress address;
    private final int prefix;
    private IPAddress high;
    private IPAddress low;

    /**
     * Creates a new address range from its string representation. This can be either a CIDR or subnet notation.
     * <br/>
     * Examples:
     * <pre>
     * 192.168.1.1/24
     * 192.168.1.1/255.255.255.0
     * 2001:db8:abcd:0012::0/64
     * 2001:db8:abcd:0012::0/ffff:ffff:ffff:ffff::
     * </pre>
     *
     * @param string the string representation
     */
    public IPAddressRange(String string) {
        String[] split = Objects.requireNonNull(string).split(RANGE_SEPARATOR, 2);

        this.address = new IPAddress(split[0]);

        if (split.length == 1) {
            this.prefix = this.address.getBitSize();
        } else {
            Integer sn = Ints.tryParse(split[1]);
            if (sn == null) {
                this.prefix = IPAddressRange.getPrefixForMask(new IPAddress(split[1]));
            } else {
                this.prefix = sn;
            }
        }
        init();
    }

    /**
     * Creates a new address range from an address an a subnet mask.
     *
     * @param address the address
     * @param mask    the subnet mask
     */
    public IPAddressRange(IPAddress address, IPAddress mask) {
        this(address, getPrefixForMask(mask));
    }

    /**
     * Creates a new address range from the an address and an prefix.
     *
     * @param address the address
     * @param prefix  the prefix
     */
    public IPAddressRange(IPAddress address, int prefix) {
        this.address = Objects.requireNonNull(address);
        this.prefix = prefix;
        init();
    }

    private void init() {

        if (this.prefix < 0 || this.prefix > this.address.getBitSize()) {
            throw new IllegalArgumentException(NOT_VALID_ERR_MSG);
        }

        byte[] bytes = this.address.getBytes();
        byte[] maskBytes = getMask().getBytes();
        byte[] highBytes = new byte[bytes.length];
        byte[] lowBytes = new byte[bytes.length];

        for (int i = 0; i < bytes.length; ++i) {
            highBytes[i] = (byte) (bytes[i] + ~maskBytes[i]);
            lowBytes[i] = (byte) (bytes[i] & maskBytes[i]);
        }

        this.low = new IPAddress(lowBytes);
        this.high = new IPAddress(highBytes);
    }

    /**
     * Checks if this address range is IPv4.
     *
     * @return if it is IPv4
     */
    public boolean isIPv4() {
        return this.address.isIPv4();
    }

    /**
     * Checks if this address range is IPv6.
     *
     * @return if it is IPv6
     */
    public boolean isIPv6() {
        return this.address.isIPv6();
    }

    /**
     * @return the IP address
     */
    public IPAddress getAddress() {
        return this.address;
    }

    /**
     * @return the subnet mask
     */
    public int getPrefix() {
        return this.prefix;
    }

    /**
     * Get the mask described by this prefix.
     *
     * @return the mask
     */
    public IPAddress getMask() {
        return getMaskForPrefix(this.prefix, this.address.getBitSize());
    }

    /**
     * @return the highest IP address in this range
     */
    public IPAddress getHigh() {
        return this.high;
    }

    /**
     * @return the lowest IP address in this range
     */
    public IPAddress getLow() {
        return this.low;
    }

    /**
     * Checks if a given IP address is in this range.
     *
     * @param ip the address
     *
     * @return whether this range contains the address
     */
    public boolean contains(IPAddress ip) {
        return ip.compareTo(getLow()) >= 0 &&
               ip.compareTo(getHigh()) <= 0;
    }

    /**
     * @return this address range as a {@link Range}
     */
    public Range<IPAddress> asRange() {
        return Range.closed(getLow(), getHigh());
    }

    @Override
    @Deprecated
    public boolean apply(IPAddress input) {
        return test(input);
    }

    @Override
    public boolean test(IPAddress input) {
        return input == null ? false : contains(input);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getPrefix());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IPAddressRange) {
            IPAddressRange other = (IPAddressRange) obj;
            return Objects.equals(getAddress(), other.getAddress()) &&
                   Objects.equals(getPrefix(), other.getPrefix());
        }
        return false;
    }

    @Override
    public String toString() {
        return getAddress() + RANGE_SEPARATOR + getPrefix();
    }

    @VisibleForTesting
    static int getPrefixForMask(IPAddress mask) {
        int prefix = 0;
        boolean zero = false;
        for (byte b : mask.getBytes()) {
            for (int i = 1; i <= Byte.SIZE; i++) {
                int result = b & (1 << (Byte.SIZE - i));
                if (result == 0) {
                    zero = true;
                } else if (zero) {
                    throw new IllegalArgumentException("Invalid netmask.");
                } else {
                    prefix++;
                }
            }
        }
        return prefix;
    }

    @VisibleForTesting
    static IPAddress getMaskForPrefix(int prefix, int numBits) {
        if (prefix < 0 || prefix > numBits) {
            throw new IllegalArgumentException();
        }
        switch (numBits) {
            case IPAddress.IPV6_BIT_SIZE:
                if (prefix == IPAddress.IPV6_BIT_SIZE) {
                    return new IPAddress(MAX_UNSIGNED_LONG, MAX_UNSIGNED_LONG);
                } else if (prefix == Long.SIZE) {
                    return new IPAddress(MAX_UNSIGNED_LONG, 0L);
                } else if (prefix == 0) {
                    return new IPAddress(0, 0);
                } else if (prefix > Long.SIZE) {
                    return new IPAddress(MAX_UNSIGNED_LONG, MAX_UNSIGNED_LONG << (IPAddress.IPV6_BIT_SIZE - prefix));
                } else {
                    return new IPAddress(MAX_UNSIGNED_LONG << (Long.SIZE - prefix), 0L);
                }
            case IPAddress.IPV4_BIT_SIZE:
                if (prefix == 0) {
                    return new IPAddress(0);
                } else {
                    return new IPAddress(MAX_UNSIGNED_INT << (IPAddress.IPV4_BIT_SIZE - prefix));
                }
            default:
                throw new IllegalArgumentException();
        }
    }
}
