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
import java.util.Set;

import org.n52.shetland.aqd.AqdConstants;
import org.n52.shetland.util.CollectionHelper;
import org.n52.svalbard.util.CodingHelper;

/**
 * {@link String} decoder for AQD e-Reporting requests
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 5.0.0
 *
 */
public class AqdStringDecoderv10 extends AbstractStringRequestDecoder {

    private static final Set<DecoderKey> DECODER_KEYS = CollectionHelper.union(
            CodingHelper.xmlStringDecoderKeysForOperationAndMediaType(AqdConstants.AQD, AqdConstants.VERSION,
                    AqdConstants.Operations.GetCapabilities, AqdConstants.Operations.GetObservation,
                    AqdConstants.Operations.DescribeSensor),
            CodingHelper.xmlStringDecoderKeysForOperationAndMediaType(AqdConstants.AQD, null,
                    AqdConstants.Operations.GetCapabilities));

    @Override
    public Set<DecoderKey> getKeys() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }
}
