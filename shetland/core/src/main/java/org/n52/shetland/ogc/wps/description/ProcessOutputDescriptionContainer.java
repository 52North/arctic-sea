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

import org.n52.janmayen.stream.Streams;
import org.n52.shetland.ogc.ows.OwsCode;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Christian Autermann
 */
public interface ProcessOutputDescriptionContainer extends Description {

    ProcessOutputDescription getOutput(OwsCode id);

    default ProcessOutputDescription getOutput(String id) {
        return getOutput(new OwsCode(id));
    }

    Collection<? extends ProcessOutputDescription> getOutputDescriptions();

    Set<OwsCode> getOutputs();

    interface Builder<T extends ProcessOutputDescriptionContainer, B extends Builder<T, B>>
            extends Description.Builder<T, B> {
        B withOutput(ProcessOutputDescription output);

        default B withOutput(ProcessOutputDescription.Builder<?, ?> output) {
            return withOutput(output.build());
        }

        default B withOutput(Stream<ProcessOutputDescription> outputs) {
            outputs.forEach(this::withOutput);
            return self();
        }

        default B withOutput(Iterable<ProcessOutputDescription> outputs) {
            return withOutput(Streams.stream(outputs));
        }

        default B withOutput(ProcessOutputDescription... outputs) {
            return withOutput(Arrays.asList(outputs));
        }

    }

}
