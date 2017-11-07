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

import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosInsertionMetadata;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.sos.x20.SosInsertionMetadataType;
import net.opengis.swes.x20.InsertionMetadataType;

/**
 * @author <a href="mailto:e.h.juerrens@52north.org">J&uuml;rrens, Eike Hinderk</a>
 */
public class SosInsertionMetadataTypeEncoder
    extends AbstractXmlEncoder<SosInsertionMetadataType, SosInsertionMetadata> {

    private static final Set<EncoderKey> ENCODER_KEYS = Sets.newHashSet(
            new ClassToClassEncoderKey(SosInsertionMetadata.class, InsertionMetadataType.class),
            new XmlPropertyTypeEncoderKey(Sos2Constants.NS_SOS_20, SosInsertionMetadata.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public SosInsertionMetadataType encode(SosInsertionMetadata objectToEncode) throws EncodingException {
        return encode(objectToEncode, EncodingContext.empty());
    }

    @Override
    public SosInsertionMetadataType encode(SosInsertionMetadata objectToEncode, EncodingContext additionalValues)
            throws EncodingException {
        SosInsertionMetadataType simt = SosInsertionMetadataType.Factory.newInstance(getXmlOptions());
        objectToEncode.getObservationTypes().parallelStream().forEach(ot -> {
            simt.addNewObservationType().setStringValue(ot);
            }
        );
        objectToEncode.getFeatureOfInterestTypes().parallelStream().forEach(ft -> {
            simt.addNewFeatureOfInterestType().setStringValue(ft);
            }
        );
        return simt;
    }

}
