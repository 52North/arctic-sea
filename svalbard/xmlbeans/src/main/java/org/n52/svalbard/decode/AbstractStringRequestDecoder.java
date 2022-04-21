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

import javax.inject.Inject;

import org.apache.xmlbeans.XmlObject;
import org.n52.shetland.ogc.ows.service.OwsServiceCommunicationObject;
import org.n52.svalbard.decode.exception.DecodingException;
import org.n52.svalbard.decode.exception.NoDecoderForKeyException;
import org.n52.svalbard.util.CodingHelper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public abstract class AbstractStringRequestDecoder implements Decoder<OwsServiceCommunicationObject, String> {

    private DecoderRepository decoderRepository;

    @Inject
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    @Override
    public OwsServiceCommunicationObject decode(String string) throws DecodingException {
        XmlObject xml = CodingHelper.readXML(string);
        DecoderKey key = CodingHelper.getDecoderKey(xml);
        Decoder<OwsServiceCommunicationObject, XmlObject> decoder = decoderRepository.getDecoder(key);
        if (decoder == null) {
            throw new NoDecoderForKeyException(key);
        }
        return decoder.decode(xml);
    }

}
