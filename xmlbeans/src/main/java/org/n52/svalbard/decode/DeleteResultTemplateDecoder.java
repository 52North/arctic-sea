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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.SupportedType;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.drt.DeleteResultTemplateConstants;
import org.n52.shetland.ogc.sos.drt.DeleteResultTemplateRequest;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;

import net.opengis.drt.x10.DeleteResultTemplateDocument;
import net.opengis.drt.x10.DeleteResultTemplateType;
import net.opengis.drt.x10.DeleteResultTemplateType.Tuple;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk
 *         J&uuml;rrens</a>
 * @since 1.0.0
 */
public class DeleteResultTemplateDecoder
        extends AbstractXmlDecoder<XmlObject, DeleteResultTemplateRequest> {
    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(DeleteResultTemplateConstants.NS, DeleteResultTemplateDocument.class),
            CodingHelper.xmlDecoderKeysForOperation(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                    DeleteResultTemplateConstants.OPERATION_NAME));

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteResultTemplateDecoder.class);

    public DeleteResultTemplateDecoder() {
        LOGGER.info("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public DeleteResultTemplateRequest decode(XmlObject xmlObject) throws DecodingException {
        LOGGER.debug(String.format("REQUESTTYPE: %s", xmlObject != null ? xmlObject.getClass() : "null recevied"));
        XmlHelper.validateDocument(xmlObject);
        if (xmlObject instanceof DeleteResultTemplateDocument) {
            DeleteResultTemplateDocument drtd = (DeleteResultTemplateDocument) xmlObject;
            DeleteResultTemplateRequest decodedRequest = parseDeleteResultTemplate(drtd);
            LOGGER.debug(String.format("Decoded request: %s", decodedRequest));
            return decodedRequest;
        } else {
            throw new UnsupportedDecoderInputException(this, xmlObject);
        }
    }

    private DeleteResultTemplateRequest parseDeleteResultTemplate(DeleteResultTemplateDocument drtd)
            throws DecodingException {
        DeleteResultTemplateRequest request = null;

        DeleteResultTemplateType drtt = drtd.getDeleteResultTemplate();

        request = new DeleteResultTemplateRequest();
        request.setVersion(drtt.getVersion());
        request.setService(drtt.getService());
        if (drtt.sizeOfResultTemplateArray() > 0) {
            parseResultTemplates(drtt.getResultTemplateArray(), request);
        }
        if (drtt.sizeOfTupleArray() > 0) {
            parseObservedPropertyOfferingPairs(drtt.getTupleArray(), request);
        }

        return request;
    }

    private void parseResultTemplates(String[] resultTemplateArray, DeleteResultTemplateRequest request) {
        for (String resultTemplateId : resultTemplateArray) {
            request.addResultTemplate(resultTemplateId);
        }
    }

    @Override
    public Set<SupportedType> getSupportedTypes() {
        return Collections.emptySet();
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    private void parseObservedPropertyOfferingPairs(Tuple[] tuples, DeleteResultTemplateRequest request) {
        for (Tuple tuple : tuples) {
            request.addObservedPropertyOfferingPair(tuple.getObservedProperty(), tuple.getOffering());
        }
    }

}
