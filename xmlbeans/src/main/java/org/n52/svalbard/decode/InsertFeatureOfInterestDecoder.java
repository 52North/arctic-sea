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
import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.ifoi.InsertFeatureOfInterestConstants;
import org.n52.shetland.ogc.sos.ifoi.InsertFeatureOfInterestRequest;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;

import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.ifoi.x10.InsertFeatureOfInterestDocument;
import net.opengis.ifoi.x10.InsertFeatureOfInterestType;

/**
 * @since 1.0.0
 */
public class InsertFeatureOfInterestDecoder
        extends AbstractXmlDecoder<XmlObject, InsertFeatureOfInterestRequest> {

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.decoderKeysForElements(InsertFeatureOfInterestConstants.NS_IFOI,
                    InsertFeatureOfInterestDocument.class),
            CodingHelper.xmlDecoderKeysForOperation(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                    InsertFeatureOfInterestConstants.OPERATION_NAME));

    private static final Logger LOGGER = LoggerFactory.getLogger(InsertFeatureOfInterestDecoder.class);

    public InsertFeatureOfInterestDecoder() {
        LOGGER.info("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    public InsertFeatureOfInterestRequest decode(XmlObject xmlObject) throws DecodingException {
        LOGGER.debug(String.format("REQUESTTYPE: %s", xmlObject != null ? xmlObject.getClass() : "null recevied"));
        // XmlHelper.validateDocument(xmlObject);
        if (xmlObject instanceof InsertFeatureOfInterestDocument) {
            InsertFeatureOfInterestDocument ifoid = (InsertFeatureOfInterestDocument) xmlObject;
            InsertFeatureOfInterestRequest decodedRequest = parseInsertFeatureOfInterest(ifoid);
            LOGGER.debug(String.format("Decoded request: %s", decodedRequest));
            return decodedRequest;
        } else {
            throw new UnsupportedDecoderInputException(this, xmlObject);
        }
    }

    private InsertFeatureOfInterestRequest parseInsertFeatureOfInterest(InsertFeatureOfInterestDocument ifoid)
            throws DecodingException {
        InsertFeatureOfInterestRequest request = null;

        InsertFeatureOfInterestType ifoit = ifoid.getInsertFeatureOfInterest();

        if (ifoit != null) {
            request = new InsertFeatureOfInterestRequest();
            request.setVersion(ifoit.getVersion());
            request.setService(ifoit.getService());
            if (CollectionHelper.isNotNullOrEmpty(ifoit.getFeatureMemberArray())) {
                parseFeatureMember(ifoit, request);
            }
        } else {
            throw new DecodingException(
                    "Received XML document is not valid. Set log level to debug to get more details");
        }

        return request;
    }

    private void parseFeatureMember(InsertFeatureOfInterestType ifoit, InsertFeatureOfInterestRequest request)
            throws DecodingException {
        for (FeaturePropertyType fpt : ifoit.getFeatureMemberArray()) {
            final Object decodedObject = decodeXmlElement(fpt);
            if (decodedObject != null && decodedObject instanceof AbstractFeature) {
                request.addFeatureMember((AbstractFeature) decodedObject);
            }
        }
    }

    public Set<SupportedType> getSupportedTypes() {
        return Collections.emptySet();
    }

    public Set<String> getConformanceClasses() {
        return Sets.newHashSet(InsertFeatureOfInterestConstants.CONFORMANCE_CLASS);
    }

}
