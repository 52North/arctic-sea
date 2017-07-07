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

import org.n52.shetland.iso.GcoConstants;
import org.n52.shetland.iso.gmd.LocalisedCharacterString;
import org.n52.shetland.iso.gmd.PT_FreeText;
import org.n52.svalbard.decode.exception.DecodingException;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class FreeTextReader extends XmlReader<PT_FreeText> {

    private PT_FreeText freeText;

    @Override
    protected void read(QName name)
            throws XMLStreamException, DecodingException {
        if (name.equals(GcoConstants.QN_GCO_CHARACTER_STRING)) {
            this.freeText = new PT_FreeText().addTextGroup(new LocalisedCharacterString(chars()));
        } else {
            ignore();
        }
    }

    @Override
    protected PT_FreeText finish()
            throws DecodingException {
        return freeText;
    }

}
