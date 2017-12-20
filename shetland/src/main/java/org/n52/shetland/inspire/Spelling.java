/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.inspire;


import org.n52.shetland.w3c.Nillable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class Spelling {

    private String text;
    private Nillable<String> script = Nillable.missing();
    private Nillable<String> transliterationScheme = Nillable.missing();

    public String getText() {
        if (Strings.isNullOrEmpty(text)) {
            return "unknown";
        }
        return text;
    }

    public Spelling setText(String text) {
        this.text = Preconditions.checkNotNull(text);
        return this;
    }

    public Nillable<String> getScript() {
        return script;
    }

    public Spelling setScript(Nillable<String> script) {
        this.script = Preconditions.checkNotNull(script);
        return this;
    }

    public Spelling setScript(String script) {
        return setScript(Nillable.of(script));
    }

    public Nillable<String> getTransliterationScheme() {
        return transliterationScheme;
    }

    public Spelling setTransliterationScheme(Nillable<String> transliterationScheme) {
        this.transliterationScheme = Preconditions.checkNotNull(transliterationScheme);
        return this;
    }

    public Spelling setTransliterationScheme(String transliterationScheme) {
        return this.setTransliterationScheme(Nillable.of(transliterationScheme));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getText(), getScript(), getTransliterationScheme());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("text", getText())
                .add("script", getScript())
                .add("transliterationScheme", getTransliterationScheme())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Spelling) {
            Spelling that = (Spelling) obj;
            return Objects.equal(this.getText(), that.getText()) &&
                   Objects.equal(this.getScript(), that.getScript()) &&
                   Objects.equal(this.getTransliterationScheme(), that.getTransliterationScheme());
        }
        return false;
    }





}
