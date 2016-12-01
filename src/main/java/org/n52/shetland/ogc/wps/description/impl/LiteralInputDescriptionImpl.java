/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps.description.impl;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;
import org.n52.shetland.ogc.wps.InputOccurence;
import org.n52.shetland.ogc.wps.description.LiteralDataDomain;
import org.n52.shetland.ogc.wps.description.LiteralInputDescription;

import com.google.common.collect.ImmutableSet;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class LiteralInputDescriptionImpl
        extends AbstractProcessInputDescription
        implements LiteralInputDescription {

    private final LiteralDataDomain defaultLiteralDataDomain;
    private final Set<LiteralDataDomain> supportedLiteralDataDomains;

    protected LiteralInputDescriptionImpl(
            AbstractBuilder<?, ?> builder) {
        this(builder.getId(),
             builder.getTitle(),
             builder.getAbstract(),
             builder.getKeywords(),
             builder.getMetadata(),
             new InputOccurence(builder.getMinimalOccurence(),
                                builder.getMaximalOccurence()),
             builder.getDefaultLiteralDataDomain(),
             builder.getSupportedLiteralDataDomains());
    }

    public LiteralInputDescriptionImpl(OwsCode id, OwsLanguageString title,
                                       OwsLanguageString abstrakt,
                                       Set<OwsKeyword> keywords,
                                       Set<OwsMetadata> metadata,
                                       InputOccurence occurence,
                                       LiteralDataDomain defaultLiteralDataDomain,
                                       Set<LiteralDataDomain> supportedLiteralDataDomains) {
        super(id, title, abstrakt, keywords, metadata, occurence);
        this.defaultLiteralDataDomain = Objects.requireNonNull(defaultLiteralDataDomain, "defaultLiteralDataDomain");
        this.supportedLiteralDataDomains = supportedLiteralDataDomains == null ? Collections.emptySet()
                                                   : supportedLiteralDataDomains;
    }

    @Override
    public LiteralDataDomain getDefaultLiteralDataDomain() {
        return this.defaultLiteralDataDomain;
    }

    @Override
    public Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
        return Collections.unmodifiableSet(supportedLiteralDataDomains);
    }

    public static abstract class AbstractBuilder<T extends LiteralInputDescription, B extends LiteralInputDescription.Builder<T, B>>
            extends AbstractProcessInputDescription.AbstractBuilder<T, B>
            implements LiteralInputDescription.Builder<T, B> {

        private LiteralDataDomain defaultLiteralDataDomain;
        private final ImmutableSet.Builder<LiteralDataDomain> supportedLiteralDataDomains
                = ImmutableSet.builder();

        public LiteralDataDomain getDefaultLiteralDataDomain() {
            return this.defaultLiteralDataDomain;
        }

        public Set<LiteralDataDomain> getSupportedLiteralDataDomains() {
            return this.supportedLiteralDataDomains.build();
        }

        @Override
        @SuppressWarnings("unchecked")
        public B withDefaultLiteralDataDomain(
                LiteralDataDomain literalDataDomain) {
            this.defaultLiteralDataDomain = Objects
                    .requireNonNull(literalDataDomain);
            return (B) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public B withSupportedLiteralDataDomain(LiteralDataDomain domain) {
            if (domain != null) {
                this.supportedLiteralDataDomains.add(domain);
            }
            return (B) this;
        }

    }

    public static class Builder extends AbstractBuilder<LiteralInputDescription, Builder> {
        @Override
        public LiteralInputDescription build() {
            return new LiteralInputDescriptionImpl(this);
        }
    }

}
