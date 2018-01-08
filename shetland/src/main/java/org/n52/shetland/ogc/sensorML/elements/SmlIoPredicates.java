/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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

import java.util.function.Predicate;

public final class SmlIoPredicates {

    private SmlIoPredicates() {
    }

    public static Predicate<SmlIo> identifier(String identifier) {
        return new IdentifierPredicate(identifier);
    }

    public static Predicate<SmlIo> name(String name) {
        return new NamePredicate(name);
    }

    public static Predicate<SmlIo> definition(String definition) {
        return new DefinitionPredicate(definition);
    }

    public static Predicate<SmlIo> nameOrDefinition(String name,
                                                            String definition) {
        return name(name).or(definition(definition));
    }

    public static Predicate<SmlIo> identifierOrNameOrDefinition(String identifier, String name, String definition) {
        return identifier(identifier).or(name(name)).or(definition(definition));
    }

    public static Predicate<SmlIo> nameAndDefinition(String name,
                                                             String definition) {
        return name(name).and(definition(definition));
    }

    private static class DefinitionPredicate implements Predicate<SmlIo> {
        private final String definition;

        DefinitionPredicate(String definition) {
            this.definition = definition;
        }

        @Override
        public boolean test(SmlIo input) {
            return input.getIoValue().isSetDefinition() &&
                   input.getIoValue().getDefinition().equalsIgnoreCase(definition);
        }
    }

    private static class NamePredicate implements Predicate<SmlIo> {
        private final String name;

        NamePredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean test(SmlIo input) {
            return (input.isSetName() && input.getIoName().equalsIgnoreCase(name)) || (input.getIoValue().isSetName()
                    && input.getIoValue().getName().getValue().equalsIgnoreCase(name));
        }
    }

    private static class IdentifierPredicate implements Predicate<SmlIo> {
        private final String identifier;

        IdentifierPredicate(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public boolean test(SmlIo input) {
            return input.isSetHref() || (input.getIoValue().isSetIdentifier()
                    && input.getIoValue().getIdentifier().equalsIgnoreCase(identifier));
        }
    }
}
