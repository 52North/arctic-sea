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
package org.n52.svalbard.decode.inspire;

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.sos.decode.AbstractXmlDecoder;
import org.n52.sos.decode.DecoderKey;
import org.n52.sos.exception.ows.concrete.UnsupportedDecoderInputException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.w3c.Nillable;
import org.n52.svalbard.inspire.base2.DocumentCitation;
import org.n52.svalbard.inspire.ompr.InspireOMPRConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.ec.inspire.schemas.base2.x20.DocumentCitationType;
import eu.europa.ec.inspire.schemas.base2.x20.DocumentCitationType.Link;

public class DocumentCitationTypeDecoder extends AbstractXmlDecoder<DocumentCitation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentCitationTypeDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(
            InspireOMPRConstants.NS_OMPR_30, DocumentCitationType.class);

    @Override
    public Set<DecoderKey> getDecoderKeyTypes() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public DocumentCitation decode(XmlObject xmlObject)
            throws OwsExceptionReport, UnsupportedDecoderInputException {
        if (xmlObject instanceof DocumentCitationType) {
            DocumentCitation documentCitation = new DocumentCitation();
            DocumentCitationType dct = (DocumentCitationType)xmlObject;
            documentCitation.setDescription(dct.getDescription().getStringValue());
            if (dct.isNilDate()) {
                if (dct.getDate().isSetNilReason()) {
                    documentCitation.setDate(Nillable.<DateTime>nil(dct.getDate().getNilReason().toString()));
                }
            } else {
                documentCitation.setDate(new DateTime(dct.getDate().getCIDate().getDate().getDate().getTime()));
            }
            if (dct.getLinkArray() != null) {
                for (Link link : dct.getLinkArray()) {
                    if (link.isNil() && link.isSetNilReason()) {
                        documentCitation.addLink(Nillable.<String>nil(link.getNilReason().toString()));
                    } else {
                        documentCitation.addLink(link.getStringValue());
                    }
                }
            }
            return documentCitation;
        }
        throw new UnsupportedDecoderInputException(this, xmlObject);
    }

}
