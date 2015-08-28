/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.coding.encode;

import org.n52.iceland.coding.OperationKey;
import org.n52.iceland.util.http.MediaType;

import com.google.common.base.Objects;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 *
 * @since 1.0.0
 */
public class OperationRequestEncoderKey extends OperationEncoderKey  {

    public OperationRequestEncoderKey(String service, String version, String operation, MediaType contentType) {
        super(service, version, operation, contentType);
    }

    public OperationRequestEncoderKey(String service, String version, Enum<?> operation, MediaType contentType) {
        super(service, version, operation, contentType);
    }

    public OperationRequestEncoderKey(OperationKey key, MediaType contentType) {
        super(key, contentType);
    }
}
