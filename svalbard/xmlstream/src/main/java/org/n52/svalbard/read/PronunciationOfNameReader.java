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
package org.n52.svalbard.read;

import java.net.URI;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.inspire.Pronunciation;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class PronunciationOfNameReader extends XmlReader<Pronunciation> {
    private Pronunciation pronunciation;

    @Override
    protected void begin() {
        this.pronunciation = new Pronunciation();
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(AqdConstants.QN_GN_PRONUNCIATION_SOUND_LINK)) {
            this.pronunciation.setSoundLink(delegate(new NillableStringReader()).map(URI::create));
        } else if (name.equals(AqdConstants.QN_GN_PRONUNCIATION_IPA)) {
            this.pronunciation.setIPA(delegate(new NillableStringReader()));
        } else {
            ignore();
        }
    }

    @Override
    protected Pronunciation finish() {
        return this.pronunciation;
    }

}
