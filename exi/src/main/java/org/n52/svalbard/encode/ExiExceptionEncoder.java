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

import java.util.Collections;
import java.util.Set;

import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.exi.EXIObject;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * Response encoder for {@link EXIObject} and {@link OwsExceptionReport}
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class ExiExceptionEncoder
        extends ExiEncoder<OwsExceptionReport> {
    private static final EncoderKey KEY = new ExceptionEncoderKey(MediaTypes.APPLICATION_EXI);

    @Override
    public Set<EncoderKey> getKeys() {
        return Collections.singleton(KEY);
    }

    @Override
    protected EncoderKey getKey(OwsExceptionReport object) {
        return KEY;
    }

}
