/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

/**
 * Representation of a proxy chain as found in HTTP {@code X-Forwarded-For} header.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 */
public class ProxyChain {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyChain.class);
    private static final Pattern PATTERN = Pattern
            .compile("^\\s*" +
                     "(?:" +
                     "\\[(" + IPAddress.getV6Pattern().pattern() + ")\\](?::\\d+)?" +
                     "|" +
                     "(" + IPAddress.getV6Pattern().pattern() + ")" +
                     "|" +
                     "(" + IPAddress.getV4Pattern().pattern() + ")(?::\\d+)?" +
                     ")" +
                     "\\s*$");

    private final List<IPAddress> proxies;
    private final IPAddress origin;

    /**
     * Creates a new chain from a origin (the original client) and all intermediate proxies.
     *
     * @param origin  the origin
     * @param proxies the proxies
     */
    public ProxyChain(IPAddress origin, List<IPAddress> proxies) {
        if (origin == null || proxies == null) {
            throw new IllegalArgumentException();
        }
        this.proxies = new ArrayList<>(proxies);
        this.origin = origin;
    }

    /**
     * Creates a new chain from a list of addresses as found in the {@code X-Forwarded-For} header. The list has to have
     * at least one member.
     *
     * @param chain the chain
     */
    public ProxyChain(List<IPAddress> chain) {
        if (chain == null || chain.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.origin = chain.get(0);
        this.proxies = new ArrayList<>(chain.subList(1, chain.size()));
    }

    /**
     * Get the origin of the request (the clients address).
     *
     * @return the origin
     */
    public IPAddress getOrigin() {
        return origin;
    }

    /**
     * Get a list of all intermediate proxy servers.
     *
     * @return the proxies
     */
    public List<IPAddress> getProxies() {
        return Collections.unmodifiableList(proxies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrigin(), getProxies());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProxyChain) {
            ProxyChain other = (ProxyChain) obj;
            return Objects.equals(getOrigin(), other.getOrigin()) &&
                   Objects.equals(getProxies(), other.getProxies());

        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).omitNullValues()
                .add("origin", getOrigin())
                .add("proxies", getProxies())
                .toString();
    }

    /**
     * Creates a Proxy chain from the {@code X-Forwarded-For} HTTP header.
     *
     * @param header the {@code X-Forwarded-For} header
     *
     * @return a {@code ProxyChain} if the header is present, non empty and well formed.
     */
    public static Optional<ProxyChain> fromForwardedForHeader(String header) {
        return Optional.ofNullable(header)
                .map(Strings::emptyToNull)
                .map(h -> h.split(","))
                .map(Arrays::stream)
                .map(h -> h.map(ProxyChain::getIPAddress))
                .map(h -> h.collect(toList()))
                .map(ProxyChain::new);

    }

    @VisibleForTesting
    static IPAddress getIPAddress(String address) {
        Matcher matcher = PATTERN.matcher(address);
        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); ++i) {
                if (matcher.group(i) != null) {
                    try {
                        return new IPAddress(matcher.group(i));
                    } catch (IllegalArgumentException e) {
                        LOG.warn("Ignoring invalid IP address in X-Forwared-For header: " + address, e);
                        return null;
                    }
                }
            }
        }
        LOG.warn("Ignoring invalid IP address in X-Forwared-For header: {}", address);
        return null;

    }
}
