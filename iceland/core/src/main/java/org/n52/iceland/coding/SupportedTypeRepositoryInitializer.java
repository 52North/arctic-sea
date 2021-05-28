/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.iceland.coding;

import javax.inject.Inject;

import org.n52.janmayen.lifecycle.Constructable;
import org.n52.svalbard.decode.DecoderRepository;
import org.n52.svalbard.encode.EncoderRepository;

public class SupportedTypeRepositoryInitializer implements Constructable {

    private final EncoderRepository encoderRepository;
    private final DecoderRepository decoderRepository;
    private final SupportedTypeRepository supportedTypeRepository;

    @Inject
    public SupportedTypeRepositoryInitializer(EncoderRepository encoderRepository,
                                               DecoderRepository decoderRepository,
                                               SupportedTypeRepository responseFormatRepository) {
        this.encoderRepository = encoderRepository;
        this.decoderRepository = decoderRepository;
        this.supportedTypeRepository = responseFormatRepository;
    }

    @Override
    public void init() {
        this.supportedTypeRepository.init(this.decoderRepository, this.encoderRepository);
    }

}