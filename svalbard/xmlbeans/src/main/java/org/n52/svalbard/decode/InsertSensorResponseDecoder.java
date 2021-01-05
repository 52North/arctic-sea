/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import net.opengis.swes.x20.InsertSensorResponseDocument;
import net.opengis.swes.x20.InsertSensorResponseType;

import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.InsertSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.base.Joiner;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class InsertSensorResponseDecoder extends AbstractXmlDecoder<XmlObject, InsertSensorResponse> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertSensorResponseDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(SwesConstants.NS_SWES_20,
                    InsertSensorResponseDocument.class),
            CodingHelper.xmlDecoderKeysForOperation(Sos2Constants.SOS, Sos2Constants.SERVICEVERSION,
                    Sos2Constants.Operations.InsertSensor));

    public InsertSensorResponseDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public InsertSensorResponse decode(XmlObject xmlObject) throws DecodingException {
        LOGGER.debug("REQUESTTYPE: {}", xmlObject != null ? xmlObject.getClass() : "null recevied");
        if (!(xmlObject instanceof InsertSensorResponseDocument)) {
            throw new UnsupportedDecoderInputException(this, xmlObject);
        }
        InsertSensorResponseDocument isrd = (InsertSensorResponseDocument) xmlObject;
        InsertSensorResponseType isr = isrd.getInsertSensorResponse();
        if (isr == null) {
            throw new DecodingException(
                    "Received XML document is not valid. Set log level to debug to get more details");
        }
        InsertSensorResponse decodedResponse = new InsertSensorResponse(SosConstants.SOS, Sos2Constants.SERVICEVERSION);
        decodedResponse.setAssignedOffering(isr.getAssignedOffering());
        decodedResponse.setAssignedProcedure(isr.getAssignedProcedure());
        return decodedResponse;

    }

}