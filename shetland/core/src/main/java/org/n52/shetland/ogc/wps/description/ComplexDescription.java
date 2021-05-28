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
package org.n52.shetland.ogc.wps.description;

import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.wps.Format;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ComplexDescription {

    Format getDefaultFormat();

    Set<Format> getSupportedFormats();

    Optional<BigInteger> getMaximumMegabytes();

    interface Builder<T extends ComplexDescription, B extends Builder<T, B>> extends org.n52.janmayen.Builder<T, B> {
        B withDefaultFormat(Format format);

        B withSupportedFormat(Format format);

        default B withSupportedFormat(Iterable<Format> formats) {
            Streams.stream(formats).forEach(this::withSupportedFormat);
            return self();
        }

        default B withSupportedFormat(Format... formats) {
            return withSupportedFormat(Arrays.asList(formats));
        }

        B withMaximumMegabytes(BigInteger maximumMegabytes);

        default B withMaximumMegabytes(long maximumMegabytes) {
            return withMaximumMegabytes(BigInteger.valueOf(maximumMegabytes));
        }
    }

}
