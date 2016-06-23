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
import java.util.Objects;

import com.google.common.base.Strings;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class OwsOperation implements Comparable<OwsOperation> {
    private final String name;
    private final List<OwsDomain> parameters;
    private final List<OwsDomain> constraints;
    private final List<OwsMetadata> metadata;
    private final List<OwsDCP> dcp;

    public OwsOperation(String name,
                        List<OwsDomain> parameters,
                        List<OwsDomain> constraints,
                        List<OwsMetadata> metadata,
                        List<OwsDCP> dcp) {
        this.name = Objects.requireNonNull(Strings.emptyToNull(name));
        this.parameters = parameters == null ? Collections.emptyList()
                                  : parameters;
        this.constraints = constraints == null ? Collections.emptyList()
                                   : constraints;
        this.metadata = metadata == null ? Collections.emptyList() : metadata;
        this.dcp = dcp == null ? Collections.emptyList() : dcp;
    }

    public String getName() {
        return this.name;
    }

    public List<OwsDomain> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    public List<OwsDomain> getConstraints() {
        return Collections.unmodifiableList(this.constraints);
    }

    public List<OwsMetadata> getMetadata() {
        return Collections.unmodifiableList(this.metadata);
    }

    public List<OwsDCP> getDCP() {
        return Collections.unmodifiableList(this.dcp);
    }

    @Override
    public int compareTo(OwsOperation o) {
        return getName().compareTo(o.getName());
    }

}
