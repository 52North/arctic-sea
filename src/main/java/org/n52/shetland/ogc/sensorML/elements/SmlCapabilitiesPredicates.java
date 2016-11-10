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
package org.n52.shetland.ogc.sensorML.elements;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class SmlCapabilitiesPredicates {
    private SmlCapabilitiesPredicates() {
    }

    public static Predicate<SmlCapabilities> name(String name) {
        return new NamePredicate(name);
    }

    private static class NamePredicate implements Predicate<SmlCapabilities> {
        private final String name;

        NamePredicate(String name) {
            this.name = Preconditions.checkNotNull(name);
        }

        @Override
        public boolean apply(SmlCapabilities input) {
            return name.equals(input.getName());
        }
    }
}
