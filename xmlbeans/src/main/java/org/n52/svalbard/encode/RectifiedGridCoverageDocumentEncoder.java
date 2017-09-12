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

import net.opengis.gml.x32.RectifiedGridCoverageDocument;

import org.n52.shetland.ogc.gml.GmlConstants;
import org.n52.shetland.ogc.om.values.RectifiedGridCoverage;
import org.n52.svalbard.encode.exception.EncodingException;

import com.google.common.collect.Sets;

/**
 * {@link Encoder} implementation for {@link RectifiedGridCoverage} to
 * {@link RectifiedGridCoverageDocument}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class RectifiedGridCoverageDocumentEncoder
        extends AbstractRectifiedGridCoverageTypeEncoder<RectifiedGridCoverageDocument> {

    private static final Set<EncoderKey> ENCODER_KEYS = Sets.newHashSet(
            new ClassToClassEncoderKey(RectifiedGridCoverageDocument.class, RectifiedGridCoverage.class),
            new XmlPropertyTypeEncoderKey(GmlConstants.NS_GML_32, RectifiedGridCoverage.class));

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public RectifiedGridCoverageDocument encode(RectifiedGridCoverage rectifiedGridCoverage)
            throws EncodingException {
        return encode(rectifiedGridCoverage, EncodingContext.empty());
    }

    @Override
    public RectifiedGridCoverageDocument encode(RectifiedGridCoverage rectifiedGridCoverage,
            EncodingContext ec) throws EncodingException {
        RectifiedGridCoverageDocument rgcd = RectifiedGridCoverageDocument.Factory.newInstance();
        rgcd.setRectifiedGridCoverage(encodeRectifiedGridCoverage(rectifiedGridCoverage, ec));
        return rgcd;
    }
}
