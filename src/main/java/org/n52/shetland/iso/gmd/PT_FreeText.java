/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.iso.gmd;

import java.util.Set;

import org.n52.shetland.util.CollectionHelper;

import com.google.common.collect.Sets;

public class PT_FreeText extends AbtractGmd {

    private Set<LocalisedCharacterString> textGroup = Sets.newHashSet();

    /**
     * @return the textGroup
     */
    public Set<LocalisedCharacterString> getTextGroup() {
        return textGroup;
    }

    /**
     * @param textGroup the textGroup to set
     */
    public PT_FreeText setTextGroup(Set<LocalisedCharacterString> textGroup) {
        this.textGroup.clear();
        this.textGroup.addAll(textGroup);
        return this;
    }

    /**
     * @param textGroup the textGroup to add
     */
    public PT_FreeText addTextGroup(LocalisedCharacterString textGroup) {
        this.textGroup.add(textGroup);
        return this;
    }

    public boolean isSetTextGroup() {
        return CollectionHelper.isNotEmpty(getTextGroup());
    }

}
