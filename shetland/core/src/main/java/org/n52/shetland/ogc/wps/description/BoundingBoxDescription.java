/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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
import org.n52.shetland.ogc.ows.OwsCRS;

import java.net.URI;
import java.util.Arrays;
import java.util.Set;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface BoundingBoxDescription {

    OwsCRS getDefaultCRS();

    Set<OwsCRS> getSupportedCRS();

    interface Builder<T extends BoundingBoxDescription, B extends Builder<T, B>>
            extends org.n52.janmayen.Builder<T, B> {

        B withDefaultCRS(OwsCRS defaultCRS);

        default B withDefaultCRS(URI defaultCRS) {
            return withDefaultCRS(defaultCRS == null ? null : new OwsCRS(defaultCRS));
        }

        default B withSupportedCRS(Iterable<OwsCRS> crss) {
            Streams.stream(crss).forEach(this::withSupportedCRS);
            return self();
        }

        default B withSupportedCRS(OwsCRS... crss) {
            return withSupportedCRS(Arrays.asList(crss));
        }

        default B withSupportedCRS(URI crs) {
            return withSupportedCRS(crs == null ? null : new OwsCRS(crs));
        }

        B withSupportedCRS(OwsCRS uom);
    }
}
