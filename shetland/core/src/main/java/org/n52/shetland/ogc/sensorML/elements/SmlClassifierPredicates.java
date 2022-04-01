/*
 * Copyright 2015-2022 52°North Initiative for Geospatial Open Source
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


/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public final class SmlClassifierPredicates {
    private SmlClassifierPredicates() {
    }

    public static Predicate<SmlClassifier> name(String name) {
        return new NamePredicate(name);
    }

    public static Predicate<SmlClassifier> definition(String definition) {
        return new DefinitionPredicate(definition);
    }

    private static class NamePredicate implements Predicate<SmlClassifier> {
        private final String name;

        NamePredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean test(SmlClassifier input) {
            return name.equals(input.getName());
        }
    }

    private static class DefinitionPredicate implements Predicate<SmlClassifier> {
        private final String definition;

        DefinitionPredicate(String definition) {
            this.definition = definition;
        }

        @Override
        public boolean test(SmlClassifier input) {
            return definition.equals(input.getDefinition());
        }
    }
}
