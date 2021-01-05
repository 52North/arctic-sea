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

import com.google.common.base.Strings;
import org.n52.shetland.ogc.ows.OwsCode;
import org.n52.shetland.ogc.ows.OwsKeyword;
import org.n52.shetland.ogc.ows.OwsLanguageString;
import org.n52.shetland.ogc.ows.OwsMetadata;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface Description {

    OwsCode getId();

    Optional<OwsLanguageString> getAbstract();

    OwsLanguageString getTitle();

    Set<OwsKeyword> getKeywords();

    Set<OwsMetadata> getMetadata();

    interface Builder<T extends Description, B extends Builder<T, B>>
            extends org.n52.janmayen.Builder<T, B> {

        default B withDescription(Description description) {
            return withIdentifier(description.getId())
                           .withTitle(description.getTitle())
                           .withAbstract(description.getAbstract().orElse(null))
                           .withKeywords(description.getKeywords())
                           .withMetadata(description.getMetadata());
        }

        B withAbstract(OwsLanguageString abstrakt);

        default B withAbstract(String abstrakt) {
            return withAbstract(Strings.emptyToNull(abstrakt) == null ? null : new OwsLanguageString(abstrakt));
        }

        default B withAbstract(String lang, String abstrakt) {
            return withAbstract(Strings.emptyToNull(abstrakt) == null ? null : new OwsLanguageString(lang, abstrakt));
        }

        B withIdentifier(OwsCode id);

        default B withIdentifier(String id) {
            return withIdentifier(new OwsCode(id));
        }

        default B withIdentifier(String codespace, String id) {
            return withIdentifier(new OwsCode(id, codespace));
        }

        default B withIdentifier(URI codespace, String id) {
            return withIdentifier(new OwsCode(id, codespace));
        }

        B withTitle(OwsLanguageString title);

        default B withTitle(String title) {
            return withTitle(null, title);
        }

        default B withTitle(String lang, String title) {
            return withTitle(Strings.emptyToNull(title) == null ? null : new OwsLanguageString(lang, title));
        }

        B withKeyword(OwsKeyword keyword);

        default B withKeyword(String keyword) {
            return withKeyword(new OwsKeyword(keyword));
        }

        default B withKeywords(Iterable<OwsKeyword> keywords) {
            keywords.forEach(this::withKeyword);
            return self();
        }

        default B withKeywords(OwsKeyword... keywords) {
            return withKeywords(Arrays.asList(keywords));
        }

        B withMetadata(OwsMetadata metadata);

        default B withMetadata(Iterable<OwsMetadata> keywords) {
            keywords.forEach(this::withMetadata);
            return self();
        }

        default B withMetadata(OwsMetadata... keywords) {
            return withMetadata(Arrays.asList(keywords));
        }
    }

}
