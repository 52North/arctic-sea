/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.n52.shetland.ogc.gml.CodeType;
import org.n52.shetland.util.CollectionHelper;
import org.n52.shetland.w3c.Nillable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class GeographicalName {

    private Nillable<String> language = Nillable.missing();
    private Nillable<CodeType> nativeness = Nillable.missing();
    private Nillable<CodeType> nameStatus = Nillable.missing();
    private Nillable<String> sourceOfName = Nillable.missing();
    private Nillable<Pronunciation> pronunciation = Nillable.missing();
    private List<Spelling> spelling = new LinkedList<>();
    private Nillable<CodeType> grammaticalGender = Nillable.missing();
    private Nillable<CodeType> grammaticalNumber = Nillable.missing();

    public Nillable<String> getLanguage() {
        return language;
    }

    public GeographicalName setLanguage(Nillable<String> language) {
        this.language = Preconditions.checkNotNull(language);
        return this;
    }

    public GeographicalName setLanguage(String language) {
        return setLanguage(Nillable.of(language));
    }

    public Nillable<CodeType> getNativeness() {
        return nativeness;
    }

    public GeographicalName setNativeness(Nillable<CodeType> nativeness) {
        this.nativeness = Preconditions.checkNotNull(nativeness);
        return this;
    }

    public GeographicalName setNativeness(CodeType nativeness) {
        return setNativeness(Nillable.of(nativeness));
    }

    public Nillable<CodeType> getNameStatus() {
        return nameStatus;
    }

    public GeographicalName setNameStatus(Nillable<CodeType> nameStatus) {
        this.nameStatus = Preconditions.checkNotNull(nameStatus);
        return this;
    }

    public GeographicalName setNameStatus(CodeType nameStatus) {
        return setNameStatus(Nillable.of(nameStatus));
    }

    public Nillable<String> getSourceOfName() {
        return sourceOfName;
    }

    public GeographicalName setSourceOfName(Nillable<String> sourceOfName) {
        this.sourceOfName = Preconditions.checkNotNull(sourceOfName);
        return this;
    }

    public GeographicalName setSourceOfName(String sourceOfName) {
        return setSourceOfName(Nillable.of(sourceOfName));
    }

    public Nillable<Pronunciation> getPronunciation() {
        return pronunciation;
    }

    public GeographicalName setPronunciation(Nillable<Pronunciation> pronunciation) {
        this.pronunciation = Preconditions.checkNotNull(pronunciation);
        return this;
    }

    public GeographicalName setPronunciation(Pronunciation pronunciation) {
        return setPronunciation(Nillable.of(pronunciation));
    }

    public List<Spelling> getSpelling() {
        if (CollectionHelper.isEmpty(spelling)) {
            addSpelling(new Spelling());
        }
        return Collections.unmodifiableList(spelling);
    }

    public GeographicalName setSpelling(List<Spelling> spelling) {
        this.spelling = Preconditions.checkNotNull(spelling);
        return this;
    }

    public GeographicalName addSpelling(Spelling spelling) {
        this.spelling.add(Preconditions.checkNotNull(spelling));
        return this;
    }

    public Nillable<CodeType> getGrammaticalGender() {
        return grammaticalGender;
    }

    public GeographicalName setGrammaticalGender(Nillable<CodeType> grammaticalGender) {
        this.grammaticalGender = Preconditions.checkNotNull(grammaticalGender);
        return this;
    }

    public GeographicalName setGrammaticalGender(CodeType grammaticalGender) {
        return setGrammaticalGender(Nillable.of(grammaticalGender));
    }

    public Nillable<CodeType> getGrammaticalNumber() {
        return grammaticalNumber;
    }

    public GeographicalName setGrammaticalNumber(Nillable<CodeType> grammaticalNumber) {
        this.grammaticalNumber = Preconditions.checkNotNull(grammaticalNumber);
        return this;
    }

    public GeographicalName setGrammaticalNumber(CodeType grammaticalNumber) {
        return setGrammaticalNumber(Nillable.of(grammaticalNumber));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("language", getLanguage()).add("nativeness", getNativeness())
                .add("nameStatus", getNameStatus()).add("grammaticalGender", getGrammaticalGender())
                .add("grammaticalNumber", getGrammaticalNumber()).add("pronunciation", getPronunciation())
                .add("sourceOfName", getSourceOfName()).add("spelling", getSpelling()).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getLanguage(), getNativeness(), getNameStatus(), getGrammaticalNumber(),
                getGrammaticalGender(), getPronunciation(), getSourceOfName(), getSpelling());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GeographicalName) {
            GeographicalName that = (GeographicalName) obj;
            return Objects.equal(this.getGrammaticalGender(), that.getGrammaticalGender())
                    && Objects.equal(this.getGrammaticalNumber(), that.getGrammaticalNumber())
                    && Objects.equal(this.getLanguage(), that.getLanguage())
                    && Objects.equal(this.getNameStatus(), that.getNameStatus())
                    && Objects.equal(this.getNativeness(), that.getNativeness())
                    && Objects.equal(this.getPronunciation(), that.getPronunciation())
                    && Objects.equal(this.getSourceOfName(), that.getSourceOfName())
                    && Objects.equal(this.getSpelling(), that.getSpelling());
        }
        return false;
    }

}
