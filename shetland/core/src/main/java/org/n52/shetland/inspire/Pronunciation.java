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

import java.net.URI;

import org.n52.shetland.w3c.Nillable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class Pronunciation {

    private Nillable<String> ipa = Nillable.missing();
    private Nillable<URI> soundLink = Nillable.missing();

    public Nillable<String> getIPA() {
        return ipa;
    }

    public Pronunciation setIPA(Nillable<String> ipa) {
        this.ipa = Preconditions.checkNotNull(ipa);
        return this;
    }

    public Pronunciation setIPA(String ipa) {
        return setIPA(Nillable.of(ipa));
    }

    public Nillable<URI> getSoundLink() {
        return soundLink;
    }

    public Pronunciation setSoundLink(Nillable<URI> soundLink) {
        this.soundLink = Preconditions.checkNotNull(soundLink);
        return this;
    }

    public Pronunciation setSoundLink(URI soundLink) {
        return setSoundLink(Nillable.of(soundLink));
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getIPA(), getSoundLink());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ipa", getIPA())
                .add("soundLink", getSoundLink())
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pronunciation) {
            Pronunciation that = (Pronunciation) obj;
            return Objects.equal(this.getIPA(), that.getIPA()) &&
                   Objects.equal(this.getSoundLink(), that.getSoundLink());
        }
        return false;
    }

}
