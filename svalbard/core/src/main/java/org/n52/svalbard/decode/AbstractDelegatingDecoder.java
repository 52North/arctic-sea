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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public abstract class AbstractDelegatingDecoder<T, S> implements Decoder<T, S> {

    private DecoderRepository decoderRepository;

    @SuppressFBWarnings({ "EI_EXPOSE_REP" })
    public DecoderRepository getDecoderRepository() {
        return decoderRepository;
    }

    @Inject
    @SuppressFBWarnings({ "EI_EXPOSE_REP2" })
    public void setDecoderRepository(DecoderRepository decoderRepository) {
        this.decoderRepository = decoderRepository;
    }

    public <T, S> Decoder<T, S> getDecoder(DecoderKey key, DecoderKey... others) {
        return this.decoderRepository.getDecoder(key, others);
    }

}
