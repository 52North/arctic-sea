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

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.filter.Filter;
import org.n52.shetland.ogc.sos.ResultFilter;
import org.n52.shetland.ogc.sos.ResultFilterConstants;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.UnsupportedDecoderInputException;
import org.n52.svalbard.util.CodingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.opengis.sosrf.x20.ResultFilterDocument;
import net.opengis.sosrf.x20.ResultFilterPropertyType;
import net.opengis.sosrf.x20.ResultFilterType;

public class ResultFilterDecoderv20 extends AbstractXmlDecoder<XmlObject, ResultFilter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultFilterDecoderv20.class);

    private static final Set<DecoderKey> DECODER_KEYS =
            CodingHelper.decoderKeysForElements(ResultFilterConstants.NS_RF_20, ResultFilterDocument.class,
                    ResultFilterPropertyType.class, ResultFilterType.class);

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public ResultFilter decode(XmlObject xmlObject) throws DecodingException {
        if (xmlObject instanceof ResultFilterType) {
            return parseType((ResultFilterType) xmlObject);
        } else if (xmlObject instanceof ResultFilterPropertyType) {
            return parseType(((ResultFilterPropertyType) xmlObject).getResultFilter());
        } else if (xmlObject instanceof ResultFilterDocument) {
            return parseType(((ResultFilterDocument) xmlObject).getResultFilter());
        } else {
            throw new UnsupportedDecoderInputException(this, xmlObject);
        }
    }

    private ResultFilter parseType(ResultFilterType xmlObject) throws DecodingException {
        return new ResultFilter((Filter<?>) decodeXmlElement(xmlObject.getFilter()));
    }

    // @Override
    // public Set<String> getConformanceClasses() {
    // return Sets.newHashSet(ResultFilterConstants.CONFORMANCE_CLASS_XML_20);
    // }
}
