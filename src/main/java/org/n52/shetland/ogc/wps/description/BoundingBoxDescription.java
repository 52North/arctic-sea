/*
 * Copyright 2016 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.wps.description;

import java.net.URI;
import java.util.Arrays;
import java.util.Set;

import org.n52.shetland.ogc.ows.OwsCRS;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface BoundingBoxDescription {

    OwsCRS getDefaultCRS();

    Set<OwsCRS> getSupportedCRS();

    interface Builder<T extends BoundingBoxDescription, B extends Builder<T, B>> {

        B withDefaultCRS(OwsCRS defaultCRS);

        default B withDefaultCRS(URI defaultCRS) {
            return withDefaultCRS(defaultCRS == null ? (OwsCRS) null
                                  : new OwsCRS(defaultCRS));
        }

        @SuppressWarnings("unchecked")
        default B withSupportedCRS(Iterable<OwsCRS> crss) {
            for (OwsCRS crs : crss) {
                withSupportedCRS(crs);
            }
            return (B) this;
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
