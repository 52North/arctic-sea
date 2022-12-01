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
package org.n52.svalbard.decode;

import java.util.Collections;
import java.util.Set;

import net.opengis.gml.x32.FeaturePropertyType;
import net.opengis.sos.x20.GetFeatureOfInterestResponseDocument;
import net.opengis.sos.x20.GetFeatureOfInterestResponseType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.shetland.ogc.gml.AbstractFeature;
import org.n52.shetland.ogc.om.features.FeatureCollection;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.response.GetFeatureOfInterestResponse;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;

import com.google.common.base.Joiner;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * XML {@link Decoder} for {@link GetFeatureOfInterestResponse}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public class GetFeatureOfInterestResponseDocumentDecoder
        extends AbstractXmlDecoder<GetFeatureOfInterestResponseDocument, GetFeatureOfInterestResponse>
        implements SosResponseDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetFeatureOfInterestResponseDocumentDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(Sos2Constants.NS_SOS_20, GetFeatureOfInterestResponseDocument.class);

    public GetFeatureOfInterestResponseDocumentDecoder() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                Joiner.on(", ").join(DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    @SuppressFBWarnings("NP_LOAD_OF_KNOWN_NULL_VALUE")
    public GetFeatureOfInterestResponse decode(GetFeatureOfInterestResponseDocument gfoird) throws DecodingException {
        if (gfoird != null) {
            GetFeatureOfInterestResponse response = new GetFeatureOfInterestResponse();
            setService(response);
            setVersions(response);
            GetFeatureOfInterestResponseType gfoirt = gfoird.getGetFeatureOfInterestResponse();
            response.setExtensions(parseExtensibleResponse(gfoirt));
            response.setAbstractFeature(parseFeatures(gfoirt));
            return response;
        }
        throw new UnsupportedDecoderInputException(this, gfoird);
    }

    private AbstractFeature parseFeatures(GetFeatureOfInterestResponseType gfoirt) throws DecodingException {
        if (CollectionHelper.isNotNullOrEmpty(gfoirt.getFeatureMemberArray())) {
            if (gfoirt.getFeatureMemberArray().length == 1) {
                return (AbstractFeature) decodeXmlObject(gfoirt.getFeatureMemberArray()[0]);
            } else {
                FeatureCollection featureCollection = new FeatureCollection();
                for (FeaturePropertyType fpt : gfoirt.getFeatureMemberArray()) {
                    featureCollection.addMember((AbstractFeature) decodeXmlObject(fpt));
                }
                return featureCollection;
            }
        }
        return null;
    }

}
