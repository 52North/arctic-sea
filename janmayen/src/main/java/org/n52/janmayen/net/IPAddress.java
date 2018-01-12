/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.janmayen.net;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.regex.Pattern;

import org.n52.janmayen.Comparables;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.net.InetAddresses;

/**
 * Encapsulation of an IP address.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 */
public class IPAddress implements Comparable<IPAddress> {

    public static final int IPV6_BYTE_SIZE = 16;
    public static final int IPV4_BYTE_SIZE = 4;
    public static final int IPV6_BIT_SIZE = IPV6_BYTE_SIZE * Byte.SIZE;
    public static final int IPV4_BIT_SIZE = IPV4_BYTE_SIZE * Byte.SIZE;

    private static final String V4_PATTERN_STRING
            = "(?:(?:25[0-5]|(?:2[0-4]|1?[0-9])?[0-9])\\.){3}(?:25[0-5]|(?:2[0-4]|1?[0-9])?[0-9])";
    private static final String V6_PATTERN_STRING
            = "(?:" +
              "(?:[0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}" +
              "|" +
              "(?:[0-9a-fA-F]{1,4}:){1,7}:" +
              "|" +
              "(?:[0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}" +
              "|" +
              "(?:[0-9a-fA-F]{1,4}:){1,5}(?::[0-9a-fA-F]{1,4}){1,2}" +
              "|" +
              "(?:[0-9a-fA-F]{1,4}:){1,4}(?::[0-9a-fA-F]{1,4}){1,3}" +
              "|" +
              "(?:[0-9a-fA-F]{1,4}:){1,3}(?::[0-9a-fA-F]{1,4}){1,4}" +
              "|" +
              "(?:[0-9a-fA-F]{1,4}:){1,2}(?::[0-9a-fA-F]{1,4}){1,5}" +
              "|" +
              "[0-9a-fA-F]{1,4}:(?:(?::[0-9a-fA-F]{1,4}){1,6})" +
              "|" +
              ":(?:(?::[0-9a-fA-F]{1,4}){1,7}|:)" +
              "|" +
              "::(?:ffff(?::0{1,4})?:)?" + V4_PATTERN_STRING +
              "|" +
              "(?:[0-9a-fA-F]{1,4}:){1,4}:" + V4_PATTERN_STRING +
              ")";
    private static final Pattern V4_PATTERN = Pattern.compile(V4_PATTERN_STRING);
    private static final Pattern V6_PATTERN = Pattern.compile(V6_PATTERN_STRING, Pattern.CASE_INSENSITIVE);
    private final InetAddress address;

    /**
     * Creates a new {@code IPAddress} from an 32-Bit integer.
     *
     * @param address the address
     */
    public IPAddress(int address) {
        this.address = InetAddresses.fromInteger(address);
    }

    /**
     * Creates a new {@code IPAddress} from two 64-Bit integers.
     *
     * @param upperBits the 64 upper bits of the address
     * @param lowerBits the 64 lower bits of the address
     */
    public IPAddress(long upperBits, long lowerBits) {
        try {
            byte[] bytes = ByteBuffer.allocate(2 * Long.BYTES).putLong(upperBits).putLong(lowerBits).array();
            this.address = InetAddress.getByAddress(bytes);
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Creates a new {@code IPAddress} from an four element byte array.
     *
     * @param address the address
     */
    public IPAddress(byte[] address) {
        try {
            this.address = InetAddress.getByAddress(address);
        } catch (UnknownHostException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * Creates a new {@code IPAddress} from its string representation.
     *
     * @param address the address
     */
    public IPAddress(String address) {
        this(InetAddresses.forString(address));
    }

    /**
     * Creates a new {@code IPAddress} from an {@link InetAddress}.
     *
     * @param address the address
     */
    public IPAddress(InetAddress address) {
        this.address = Objects.requireNonNull(address);
    }

    /**
     * @return the IP address as an 32-bit integer
     *
     * @deprecated {@linkplain  Inet6Address IPv6 addresses} can not be represented by an integer
     */
    @Deprecated
    public int asInt() {
        return InetAddresses.coerceToInteger(this.address);
    }

    /**
     * Checks if this address is IPv4.
     *
     * @return if it is IPv4
     */
    public boolean isIPv4() {
        return this.address instanceof Inet4Address;
    }

    /**
     * Get the number of bits the address contains.
     *
     * @return the number of bits
     */
    public int getBitSize() {
        return getByteSize() * Byte.SIZE;
    }

    /**
     * Get the number of bytes the address contains.
     *
     * @return the number of bytes
     */
    public int getByteSize() {
        if (isIPv4()) {
            return IPV4_BYTE_SIZE;
        } else if (isIPv6()) {
            return IPV6_BYTE_SIZE;
        } else {
            throw new AssertionError();
        }
    }

    /**
     * Checks if this address is IPv6.
     *
     * @return if it is IPv6
     */
    public boolean isIPv6() {
        return this.address instanceof Inet6Address;
    }

    /**
     * @return the IP address as an {@code Inet4Address}
     *
     * @deprecated use {@link #getInetAddress() }
     */
    @Deprecated
    public InetAddress asInetAddress() {
        return getInetAddress();
    }

    /**
     * @return the IP address as an {@code Inet4Address}
     */
    public InetAddress getInetAddress() {
        return this.address;
    }

    /**
     * @return the IP address as an byte array.
     *
     * @deprecated use {@link #getBytes() }
     */
    @Deprecated
    public byte[] asByteArray() {
        return getBytes();
    }

    /**
     * @return the IP address as an byte array.
     */
    public byte[] getBytes() {
        return this.address.getAddress();
    }

    /**
     * @return the IP address as a string
     *
     * @deprecated use {@link #toString() }
     */
    @Deprecated
    public String asString() {
        return toString();
    }

    @Override
    public int compareTo(IPAddress o) {
        return compareLittleEndianUnsignedByte(getBytes(), o.getBytes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.address);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IPAddress) {
            IPAddress other = (IPAddress) obj;
            return this.address.equals(other.getInetAddress());
        }
        return false;
    }

    @Override
    public String toString() {
        return this.address.getHostAddress();
    }

    /**
     * Compares to {@code byte} arrays. Each element is compared with the corresponding element in the other array. If
     * the array sizes differ the shorter array is treated as it would contain <i>leading</i> zeros.
     *
     * @param a the first {@code byte} array
     * @param b the second {@code byte} array
     *
     * @return the value {@code 0} if {@code a == b}; a value less than {@code 0} if {@code a < b}; and a value greater
     *         than {@code 0} if {@code a > b}
     */
    @VisibleForTesting
    static int compareLittleEndianUnsignedByte(byte[] a, byte[] b) {
        int la = a.length;
        int lb = b.length;
        int i = 0;
        int dl = la - lb;

        if (dl < 0) {
            return -compareLittleEndianUnsignedByte(b, a);
        }

        for (; i < dl; ++i) {
            int result = Comparables.compareUnsignedByte(a[i], (byte) 0);
            if (result != 0) {
                return result;
            }
        }

        for (; i < la; ++i) {
            int result = Comparables.compareUnsignedByte(a[i], b[i - dl]);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    /**
     * Get the regular expression matching an IPv4. No capturing groups are included in the pattern.
     *
     * @return the pattern
     */
    public static Pattern getV4Pattern() {
        return V4_PATTERN;
    }

    /**
     * Get the regular expression matching an IPv6. No capturing groups are included in the pattern.
     *
     * @return the pattern
     */
    public static Pattern getV6Pattern() {
        return V6_PATTERN;
    }

}
