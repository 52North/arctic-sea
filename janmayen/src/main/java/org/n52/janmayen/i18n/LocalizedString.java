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
package org.n52.janmayen.i18n;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Immutable localized variant of a string.
 *
 * @author Christian Autermann
 * @since 1.0.0
 */
public class LocalizedString implements Serializable {
    private static final Locale NULL_LOCALE = new Locale("");
    private static final long serialVersionUID = 8336541273458492969L;
    private final Locale lang;
    private final String text;

    public LocalizedString(String value) {
        this(null, value);
    }

    public LocalizedString(Locale lang, String value) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value));
        this.text = value;
        this.lang = lang == null ? NULL_LOCALE : lang;
    }

    /**
     * @return the value of this localized string
     */
    public String getText() {
        return this.text;
    }

    /**
     * @return the language of this localized string
     */
    public Locale getLang() {
        return this.lang;
    }

    public String getLangString() {
        String country = this.lang.getISO3Country();
        String language = this.lang.getISO3Language();
        StringBuilder sb = new StringBuilder(language);
        if (!country.isEmpty()) {
            sb.append("-").append(country);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("lang", getLang())
                .add("text", getText())
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLang(), getText());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalizedString) {
            LocalizedString that = (LocalizedString) obj;
            return Objects.equals(this.getLang(), that.getLang()) &&
                   Objects.equals(this.getText(), that.getText());
        }
        return false;
    }

}
