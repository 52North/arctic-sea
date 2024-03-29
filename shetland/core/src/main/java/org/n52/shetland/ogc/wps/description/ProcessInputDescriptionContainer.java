/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.ows.OwsCode;

/**
 * @author Christian Autermann
 */
public interface ProcessInputDescriptionContainer extends Description {

    ProcessInputDescription getInput(OwsCode id);

    default ProcessInputDescription getInput(String id) {
        return getInput(new OwsCode(id));
    }

    Collection<? extends ProcessInputDescription> getInputDescriptions();

    Set<OwsCode> getInputs();

    interface Builder<
            T extends ProcessInputDescriptionContainer,
            B extends ProcessInputDescriptionContainer.Builder<T, B>> extends Description.Builder<T, B> {

        B withInput(ProcessInputDescription input);

        default B withInput(ProcessInputDescription.Builder<?, ?> input) {
            return withInput(input.build());
        }

        default B withInput(Stream<? extends ProcessInputDescription> input) {
            input.forEach(this::withInput);
            return self();
        }

        default B withInput(Iterable<ProcessInputDescription> inputs) {
            return withInput(Streams.stream(inputs));
        }

        default B withInput(ProcessInputDescription... inputs) {
            return withInput(Arrays.asList(inputs));
        }

    }

}
