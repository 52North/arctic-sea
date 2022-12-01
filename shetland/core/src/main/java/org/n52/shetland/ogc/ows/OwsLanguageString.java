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
package org.n52.shetland.ogc.ows;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.n52.janmayen.Optionals;
import org.n52.janmayen.i18n.LocalizedString;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsLanguageString implements Comparable<OwsLanguageString> {
    private static final Comparator<OwsLanguageString> COMPARATOR
            = Comparator.nullsLast(Comparator
                                           .comparing(OwsLanguageString::getLang, Optionals.nullsLast())
                                           .thenComparing(OwsLanguageString::getValue));
    private final String lang;
    private final String value;

    @SuppressFBWarnings({"NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE",
                         "NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE"})
    public OwsLanguageString(@Nullable String lang, String value) {
        this.lang = Strings.emptyToNull(lang);
        this.value = Objects.requireNonNull(Strings.emptyToNull(value));
    }

    public OwsLanguageString(LocalizedString s) {
        this(s.getLangString(), s.getText());
    }

    public OwsLanguageString(String value) {
        this(null, value);
    }

    public Optional<String> getLang() {
        return Optional.ofNullable(this.lang);
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.lang, this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OwsLanguageString that = (OwsLanguageString) obj;
        return Objects.equals(this.lang, that.lang) &&
               Objects.equals(this.value, that.value);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .omitNullValues()
                          .add("lang", this.lang)
                          .add("value", this.value)
                          .toString();
    }

    @Override
    public int compareTo(OwsLanguageString o) {
        return COMPARATOR.compare(this, o);
    }

}