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
package org.n52.shetland.ogc.sensorML.elements;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class SmlIdentifierPredicates {
    private SmlIdentifierPredicates() {
    }

    public static Predicate<SmlIdentifier> name(String name) {
        return new NamePredicate(name);
    }

    public static Predicate<SmlIdentifier> definition(String definition) {
        return new DefinitionPredicate(definition);
    }

    public static Predicate<SmlIdentifier> nameOrDefinition(String name,
                                                            String definition) {
        return Predicates.or(name(name), definition(definition));
    }

    public static Predicate<SmlIdentifier> nameAndDefinition(String name,
                                                             String definition) {
        return Predicates.and(name(name), definition(definition));
    }

    private static class DefinitionPredicate implements Predicate<SmlIdentifier> {
        private final String definition;

        DefinitionPredicate(String definition) {
            this.definition = definition;
        }

        @Override
        public boolean apply(SmlIdentifier input) {
            return input.isSetDefinition() &&
                   input.getDefinition().equalsIgnoreCase(definition);
        }
    }

    private static class NamePredicate implements Predicate<SmlIdentifier> {
        private final String name;

        NamePredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean apply(SmlIdentifier input) {
            return input.isSetName() &&
                   input.getName().equalsIgnoreCase(name);
        }
    }
}
