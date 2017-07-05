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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import javax.xml.stream.XMLStreamException;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import org.n52.shetland.ogc.sos.ro.RelatedOfferingConstants;
import org.n52.shetland.ogc.sos.ro.RelatedOfferings;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.write.RelatedOfferingXmlStreamWriter;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class RelatedOfferingsEncoder extends AbstractXmlEncoder<XmlObject, RelatedOfferings> {

    private static final Set<EncoderKey> ENCODER_KEYS = CodingHelper
            .encoderKeysForElements(RelatedOfferingConstants.NS_RO,
                                    RelatedOfferings.class);

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.unmodifiableSet(ENCODER_KEYS);
    }

    @Override
    public void addNamespacePrefixToMap(Map<String, String> nameSpacePrefixMap) {
        nameSpacePrefixMap.put(RelatedOfferingConstants.NS_RO, RelatedOfferingConstants.NS_RO_PREFIX);
    }

    @Override
    public XmlObject encode(RelatedOfferings objectToEncode, EncodingContext ctx)
            throws EncodingException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            EncodingContext context = ctx.with(EncoderFlags.ENCODER_REPOSITORY, getEncoderRepository())
                    .with(XmlEncoderFlags.XML_OPTIONS, (Supplier<XmlOptions>) this::getXmlOptions);

            new RelatedOfferingXmlStreamWriter(context, baos, objectToEncode).write();
            return XmlObject.Factory.parse(baos.toString("UTF8"));
        } catch (XMLStreamException | XmlException | UnsupportedEncodingException ex) {
            String message = String.format("Error encoding %s", objectToEncode.getClass().getSimpleName());
            throw new EncodingException(message, ex);
        }
    }

}
