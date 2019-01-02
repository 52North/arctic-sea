/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.ows;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.SortedSet;

import org.n52.janmayen.i18n.MultilingualString;
import org.n52.shetland.util.CollectionHelper;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class OwsDescription {

    private  Optional<MultilingualString> title;
    private  Optional<MultilingualString> abstrakt;
    private  SortedSet<OwsKeyword> keywords;

    public OwsDescription(MultilingualString title,
                          MultilingualString abstrakt,
                          Collection<OwsKeyword> keywords) {
        this.title = Optional.ofNullable(title);
        this.abstrakt = Optional.ofNullable(abstrakt);
        this.keywords = CollectionHelper.newSortedSet(keywords);
    }

    public Optional<MultilingualString> getTitle() {
        return title;
    }

    public void setTitle(MultilingualString title) {
        this.title = Optional.ofNullable(title);
    }

    public Optional<MultilingualString> getAbstract() {
        return abstrakt;
    }

    public SortedSet<OwsKeyword> getKeywords() {
        return Collections.unmodifiableSortedSet(keywords);
    }

    public void setKeywords(Collection<OwsKeyword> keywords) {
        this.keywords = CollectionHelper.newSortedSet(keywords);
    }

    public void setAbstrakt(MultilingualString abstrakt) {
        this.abstrakt = Optional.ofNullable(abstrakt);
    }

}
