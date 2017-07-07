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
package org.n52.svalbard.decode;

import java.util.Collections;
import java.util.Set;

import net.opengis.sosro.x10.RelatedOfferingsPropertyType;
import net.opengis.sosro.x10.RelatedOfferingsType.RelatedOffering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.gml.ReferenceType;
import org.n52.shetland.ogc.sos.ro.OfferingContext;
import org.n52.shetland.ogc.sos.ro.RelatedOfferingConstants;
import org.n52.shetland.ogc.sos.ro.RelatedOfferings;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.base.Joiner;

/**
 * @author Jan Schulte
 */
public class RelatedOfferingTypeDecoder extends AbstractXmlDecoder<RelatedOfferingsPropertyType, RelatedOfferings> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelatedOfferingTypeDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(
            RelatedOfferingConstants.NS_RO,
            RelatedOfferingsPropertyType.class);

    public RelatedOfferingTypeDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                     Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public RelatedOfferings decode(RelatedOfferingsPropertyType rot) throws DecodingException {
        RelatedOfferings relatedOfferings = new RelatedOfferings();
        relatedOfferings.setNamespace(RelatedOfferingConstants.NS_RO)
                .setDefinition(RelatedOfferingConstants.RELATED_OFFERINGS)
                .setIdentifier(RelatedOfferingConstants.RELATED_OFFERINGS);
        for (RelatedOffering relatedOffering : rot.getRelatedOfferings().getRelatedOfferingArray()) {
            relatedOfferings.getValue().add(parseRelatedOffering(relatedOffering));
        }
        return relatedOfferings;
    }

    private OfferingContext parseRelatedOffering(RelatedOffering relatedOffering) throws DecodingException {
        ReferenceType role = decodeXmlElement(relatedOffering.getOfferingContext().getRole());
        ReferenceType relOff = decodeXmlElement(relatedOffering.getOfferingContext().getRelatedOffering());
        return new OfferingContext(role, relOff);
    }

}
