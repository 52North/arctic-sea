/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.n52.shetland.util.CollectionHelper;

/**
 * Internal representation of the ISO GMD Telephone.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class CiTelephone extends AbstractObject {

    private List<String> voice = new ArrayList<>();
    private List<String> facsimile = new ArrayList<>();

    public boolean isSetVoice() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(voice);
    }

    public List<String> getVoice() {
        return voice;
    }

    public CiTelephone setVoice(final Collection<String> voice) {
        voice.clear();
        this.voice.addAll(voice);
        return this;
    }

    public CiTelephone addVoice(final String voice) {
        if (voice != null) {
            this.voice.add(voice);
        }
        return this;
    }

    public boolean isSetFacsimile() {
        return !CollectionHelper.nullEmptyOrContainsOnlyNulls(facsimile);
    }

    public List<String> getFacsimile() {
        return facsimile;
    }

    public CiTelephone addFacsimile(final String facsimile) {
        if (facsimile != null) {
            this.facsimile.add(facsimile);
        }
        return this;
    }

    public CiTelephone setFacsimile(final Collection<String> facsimile) {
        this.facsimile.clear();
        if (facsimile != null) {
            this.facsimile.addAll(facsimile);
        }
        return this;
    }

}
