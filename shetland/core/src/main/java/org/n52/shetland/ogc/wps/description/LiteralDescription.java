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
package org.n52.shetland.ogc.wps.description;

import org.n52.janmayen.stream.Streams;

import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface LiteralDescription {
    Set<LiteralDataDomain> getSupportedLiteralDataDomains();

    LiteralDataDomain getDefaultLiteralDataDomain();

    interface Builder<T extends LiteralDescription, B extends Builder<T, B>> extends org.n52.janmayen.Builder<T, B> {

        default B withSupportedLiteralDataDomain(Iterable<LiteralDataDomain> domains) {
            Streams.stream(domains).forEach(this::withDefaultLiteralDataDomain);
            return self();
        }

        B withSupportedLiteralDataDomain(LiteralDataDomain domain);

        default B withSupportedLiteralDataDomain(LiteralDataDomain.Builder<?, ?> domain) {
            return withSupportedLiteralDataDomain(domain.build());
        }

        B withDefaultLiteralDataDomain(LiteralDataDomain domain);

        default B withDefaultLiteralDataDomain(LiteralDataDomain.Builder<?, ?> domain) {
            return withDefaultLiteralDataDomain(domain.build());
        }

    }

}
