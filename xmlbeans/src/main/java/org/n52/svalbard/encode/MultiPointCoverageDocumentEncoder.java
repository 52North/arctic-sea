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

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.values.MultiPointCoverage;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

import net.opengis.gml.x32.MultiPointCoverageDocument;

/**
 * {@link Encoder} implementation for {@link MultiPointCoverage} to
 * {@link MultiPointCoverageDocument}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class MultiPointCoverageDocumentEncoder
        extends AbstractMultiPointCoverageTypeEncoder<MultiPointCoverageDocument> {

    private static final Set<EncoderKey> ENCODER_KEYS =
            Sets.newHashSet(new ClassToClassEncoderKey(MultiPointCoverageDocument.class, MultiPointCoverage.class),
                    new XmlPropertyTypeEncoderKey(GmlConstants.NS_GML_32, MultiPointCoverage.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public MultiPointCoverageDocument encode(MultiPointCoverage multiPointCoverage)
            throws EncodingException {
        return encode(multiPointCoverage, EncodingContext.empty());
    }

    @Override
    public MultiPointCoverageDocument encode(MultiPointCoverage multiPointCoverage,
            EncodingContext rec) throws EncodingException {
        MultiPointCoverageDocument mpcd = MultiPointCoverageDocument.Factory.newInstance();
        mpcd.setMultiPointCoverage(encodeMultiPointCoverageType(mpcd.addNewMultiPointCoverage(), multiPointCoverage));
        return mpcd;
    }

}
