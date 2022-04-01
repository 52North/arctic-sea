/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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
import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.sos.SosSpatialFilter;
import org.n52.shetland.ogc.sos.SosSpatialFilterConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opengis.sossf.x10.SpatialFilterDocument;
import net.opengis.sossf.x10.SpatialFilterPropertyType;
import net.opengis.sossf.x10.SpatialFilterType;

public class SosSpatialFilterDecoder
        extends
        AbstractXmlDecoder<XmlObject, SosSpatialFilter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SosSpatialFilterDecoder.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(SosSpatialFilterConstants.NS_SF, SpatialFilterDocument.class,
                    SpatialFilterPropertyType.class, SpatialFilterType.class);

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public SosSpatialFilter decode(XmlObject xmlObject)
            throws DecodingException {
        if (xmlObject instanceof SpatialFilterType) {
            return parseType((SpatialFilterType) xmlObject);
        } else if (xmlObject instanceof SpatialFilterPropertyType) {
            return parseType(((SpatialFilterPropertyType) xmlObject).getSpatialFilter());
        } else if (xmlObject instanceof SpatialFilterDocument) {
            return parseType(((SpatialFilterDocument) xmlObject).getSpatialFilter());
        } else {
            throw new UnsupportedDecoderInputException(this, xmlObject);
        }
    }

    private SosSpatialFilter parseType(SpatialFilterType xmlObject)
            throws DecodingException {
        return new SosSpatialFilter((SpatialFilter) decodeXmlElement(xmlObject.getSpatialOps()));
    }

    // @Override
    // public Set<String> getConformanceClasses() {
    // return Sets.newHashSet(SosSpatialFilterConstants.CONFORMANCE_CLASS_XML);
    // }
}
