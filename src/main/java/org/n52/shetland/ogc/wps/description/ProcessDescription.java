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

import static java.util.Comparator.comparing;
import static java.util.Comparator.naturalOrder;
import static java.util.Comparator.nullsLast;

import java.util.Comparator;

import org.n52.shetland.ogc.ows.OwsCode;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public interface ProcessDescription extends ProcessInputDescriptionContainer,
                                            ProcessOutputDescriptionContainer,
                                            Comparable<ProcessDescription> {

    static Comparator<ProcessDescription> COMPARATOR
            = nullsLast(comparing(ProcessDescription::getId, comparing(OwsCode::getCodeSpace, comparing(x -> x
                                                                       .orElse(null), nullsLast(naturalOrder())))
                                  .thenComparing(comparing(OwsCode::getValue)))
                    .thenComparing(comparing(ProcessDescription::getVersion, nullsLast(naturalOrder()))));

    String getVersion();

    boolean isStatusSupported();

    boolean isStoreSupported();

    @Override
    default int compareTo(ProcessDescription o) {
        return COMPARATOR.compare(this, o);
    }

    interface Builder<T extends ProcessDescription, B extends Builder<T, B>>
            extends ProcessInputDescriptionContainer.Builder<T, B>,
                    ProcessOutputDescriptionContainer.Builder<T, B> {

        B statusSupported(boolean statusSupported);

        B storeSupported(boolean storeSupported);

        B withVersion(String version);

    }

}
