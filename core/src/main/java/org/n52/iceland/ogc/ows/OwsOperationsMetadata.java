/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.ogc.ows;

import java.util.Collections;
import java.util.List;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsOperationsMetadata {

    private final List<OwsOperation> operations;
    private final List<OwsDomain> parameters;
    private final List<OwsDomain> constraints;

    public OwsOperationsMetadata(List<OwsOperation> operations,
                                 List<OwsDomain> parameters,
                                 List<OwsDomain> constraints) {
        this.operations = operations == null ? Collections.emptyList()
                          : operations;
        this.parameters = parameters == null ? Collections.emptyList()
                          : parameters;
        this.constraints = constraints == null ? Collections.emptyList()
                           : constraints;
    }

    public List<OwsOperation> getOperations() {
        return Collections.unmodifiableList(operations);
    }

    public List<OwsDomain> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public List<OwsDomain> getConstraints() {
        return Collections.unmodifiableList(constraints);
    }
}
