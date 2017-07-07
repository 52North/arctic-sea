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
package org.n52.svalbard.read;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.inspire.Spelling;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SpellingOfNameReader extends XmlReader<Spelling> {
    private Spelling spelling;

    @Override
    protected void begin() {
        this.spelling = new Spelling();
    }

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(AqdConstants.QN_GN_TEXT)) {
            this.spelling.setText(chars());
        } else if (name.equals(AqdConstants.QN_GN_SCRIPT)) {
            this.spelling.setScript(delegate(new NillableStringReader()));
        } else if (name.equals(AqdConstants.QN_GN_TRANSLITERATION_SCHEME)) {
            this.spelling.setTransliterationScheme(delegate(new NillableStringReader()));
        } else {
            ignore();
        }
    }

    @Override
    protected Spelling finish() {
        return this.spelling;
    }

}
