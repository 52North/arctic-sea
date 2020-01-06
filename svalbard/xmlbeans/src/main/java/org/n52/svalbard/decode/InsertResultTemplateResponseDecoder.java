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
package org.n52.svalbard.decode;

import java.util.Collections;
import java.util.Set;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.InsertResultTemplateResponse;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import net.opengis.sos.x20.InsertResultTemplateResponseDocument;
import net.opengis.sos.x20.InsertResultTemplateResponseType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertResultTemplateResponseDecoder extends AbstractXmlDecoder<XmlObject, InsertResultTemplateResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertResultTemplateResponseDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(Sos2Constants.NS_SOS_20,
                    InsertResultTemplateResponseDocument.class),
            CodingHelper.xmlDecoderKeysForOperation(Sos2Constants.SOS, Sos2Constants.SERVICEVERSION,
                    Sos2Constants.Operations.InsertResultTemplate));

    public InsertResultTemplateResponseDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public InsertResultTemplateResponse decode(XmlObject xmlObject) throws DecodingException {
        LOGGER.debug("REQUESTTYPE: {}", xmlObject != null ? xmlObject.getClass() : "null recevied");
        if (!(xmlObject instanceof InsertResultTemplateResponseDocument)) {
            throw new UnsupportedDecoderInputException(this, xmlObject);
        }
        InsertResultTemplateResponseDocument isrd = (InsertResultTemplateResponseDocument) xmlObject;
        InsertResultTemplateResponseType isr = isrd.getInsertResultTemplateResponse();
        if (isr == null) {
            throw new DecodingException(
                    "Received XML document is not valid. Set log level to debug to get more details");
        }
        InsertResultTemplateResponse decodedResponse = new InsertResultTemplateResponse(SosConstants.SOS,
                Sos2Constants.SERVICEVERSION);
        decodedResponse.setAcceptedTemplate(isr.getAcceptedTemplate());
        return decodedResponse;
    }
}
