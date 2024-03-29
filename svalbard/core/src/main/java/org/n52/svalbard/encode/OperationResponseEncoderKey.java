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
package org.n52.svalbard.encode;

import org.n52.janmayen.http.MediaType;
import org.n52.shetland.ogc.ows.service.OwsOperationKey;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 1.0.0
 */
public class OperationResponseEncoderKey extends OperationEncoderKey {

    public OperationResponseEncoderKey(String service, String version, String operation, MediaType contentType) {
        super(service, version, operation, contentType);
    }

    public OperationResponseEncoderKey(String service, String version, Enum<?> operation, MediaType contentType) {
        super(service, version, operation, contentType);
    }

    public OperationResponseEncoderKey(OwsOperationKey key, MediaType contentType) {
        super(key, contentType);
    }

}
