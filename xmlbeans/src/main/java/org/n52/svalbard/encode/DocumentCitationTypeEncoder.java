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
package org.n52.svalbard.encode;

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.inspire.base2.DocumentCitation;
import org.n52.shetland.inspire.base2.InspireBase2Constants;
import org.n52.shetland.w3c.Nillable;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import eu.europa.ec.inspire.schemas.base2.x20.DocumentCitationType;
import eu.europa.ec.inspire.schemas.base2.x20.DocumentCitationType.Link;

public class DocumentCitationTypeEncoder
        extends AbstractXmlEncoder<XmlObject, DocumentCitation> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            Sets.newHashSet(new ClassToClassEncoderKey(DocumentCitationType.class, DocumentCitation.class),
                    new XmlEncoderKey(InspireBase2Constants.NS_BASE2, DocumentCitation.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public XmlObject encode(DocumentCitation documentCitation, EncodingContext context) throws EncodingException {
        DocumentCitationType dct = DocumentCitationType.Factory.newInstance(getXmlOptions());
        if (documentCitation.isSetDate()) {
            dct.addNewDate().addNewCIDate().addNewDate()
                    .setDateTime(documentCitation.getDate().get().toGregorianCalendar());
        }
        if (documentCitation.isSetName()) {
            dct.setName2(documentCitation.getFirstName().getValue());
        }
        if (documentCitation.isSetLinks()) {
            for (Nillable<String> link : documentCitation.getLinks()) {
                if (link.isPresent()) {
                    dct.addNewLink().setStringValue(link.get());
                } else {
                    Link l = dct.addNewLink();
                    l.setNil();
                    if (link.getNilReason().isPresent()) {
                        l.setNilReason(link.getNilReason().get());
                    }
                }
            }
        }
        return dct;
    }

}
