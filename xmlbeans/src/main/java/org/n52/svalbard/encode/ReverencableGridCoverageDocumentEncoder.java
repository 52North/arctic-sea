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

import net.opengis.gmlcov.x10.ReferenceableGridCoverageDocument;

import org.n52.shetland.ogc.om.values.ReferencableGridCoverage;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

/**
 * {@link Encoder} implementation to encode {@link ReferencableGridCoverage} to
 * {@link ReferenceableGridCoverageDocument}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.1.0
 *
 */
public class ReverencableGridCoverageDocumentEncoder
        extends AbstractReverencableGridCoverageType<ReferenceableGridCoverageDocument> {

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.emptySet();
    }

    @Override
    public ReferenceableGridCoverageDocument encode(ReferencableGridCoverage objectToEncode) throws EncodingException {
        throw new UnsupportedEncoderInputException(this, objectToEncode);
    }

    @Override
    public ReferenceableGridCoverageDocument encode(ReferencableGridCoverage objectToEncode, EncodingContext ec)
            throws EncodingException {
        throw new UnsupportedEncoderInputException(this, objectToEncode);
    }

}
