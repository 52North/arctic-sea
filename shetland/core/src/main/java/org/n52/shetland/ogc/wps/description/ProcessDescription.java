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

import java.util.Comparator;

import org.n52.shetland.ogc.ows.OwsCode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessDescription
        extends ProcessInputDescriptionContainer, ProcessOutputDescriptionContainer, Comparable<ProcessDescription> {

    Comparator<
            ProcessDescription> COMPARATOR =
                    Comparator
                            .nullsLast(
                                    Comparator
                                            .comparing(ProcessDescription::getId,
                                                    Comparator
                                                            .comparing(OwsCode::getCodeSpace,
                                                                    Comparator.comparing(x -> x.orElse(null),
                                                                            Comparator.nullsLast(
                                                                                    Comparator.naturalOrder())))
                                                            .thenComparing(OwsCode::getValue))
                                            .thenComparing(ProcessDescription::getVersion,
                                                    Comparator.nullsLast(Comparator.naturalOrder())));

    String getVersion();

    boolean isStatusSupported();

    boolean isStoreSupported();

    Builder<?, ?> newBuilder();

    @Override
    default int compareTo(ProcessDescription o) {
        return COMPARATOR.compare(this, o);
    }

    interface Builder<
            T extends ProcessDescription,
            B extends Builder<T, B>>
            extends ProcessInputDescriptionContainer.Builder<T, B>, ProcessOutputDescriptionContainer.Builder<T, B> {

        B statusSupported(boolean statusSupported);

        B storeSupported(boolean storeSupported);

        B withVersion(String version);

    }

}
