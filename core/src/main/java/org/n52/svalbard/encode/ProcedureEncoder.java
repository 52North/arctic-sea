/*
 * Copyright 2016-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.svalbard.encode;

import java.util.Set;

import org.n52.svalbard.ProcedureCoder;

/**
 * @since 1.0.0
 *
 * @param <T> the resulting type, the "Target"
 * @param <S> the input type, the "Source"
 */
public interface ProcedureEncoder<S, T> extends ConformanceClassEncoder<S, T>, ProcedureCoder {

    /**
     * Get the supported procedure description formats for this {@linkplain ProcedureEncoder} and the specified service
     * and version.
     *
     * @param service the service
     * @param version the version
     *
     * @return the procedure description formats
     */
    @Override
    Set<String> getSupportedProcedureDescriptionFormats(String service, String version);

}
