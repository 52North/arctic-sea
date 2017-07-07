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
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import org.apache.xmlbeans.XmlObject;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.util.CodingHelper;
import org.n52.svalbard.util.XmlHelper;

/**
 * {@link XmlObject} decoder for AQD e-Reporting requests
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class AqdDecoderv10 implements Decoder<OwsServiceCommunicationObject, XmlObject> {

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.xmlDecoderKeysForOperation(AqdConstants.AQD,
            AqdConstants.VERSION, AqdConstants.Operations.GetCapabilities, AqdConstants.Operations.GetObservation,
            AqdConstants.Operations.DescribeSensor);

    private DecoderRepository decoderRepository;

    @Inject
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = Objects.requireNonNull(decoderRepository);
    }

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public OwsServiceCommunicationObject decode(XmlObject objectToDecode) throws DecodingException {
        DecoderKey key = new XmlNamespaceDecoderKey(XmlHelper.getNamespace(objectToDecode), XmlObject.class);
        Decoder<OwsServiceCommunicationObject, XmlObject> decoder = this.decoderRepository.getDecoder(key);
        return decoder.decode(objectToDecode);
    }

}
