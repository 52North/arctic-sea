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
package org.n52.iceland.statistics.api.parameters;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ObjectEsParameter extends AbstractEsParameter {
    private final List<AbstractEsParameter> children;

    public ObjectEsParameter(String name, Description description, AbstractEsParameter... abstractEsParameters) {
        super(name, description);
        children = Arrays.asList(abstractEsParameters);
    }

    public ObjectEsParameter(String name, AbstractEsParameter... abstractEsParameters) {
        super(name);
        children = Arrays.asList(abstractEsParameters);
    }

    @Override
    public List<AbstractEsParameter> getAllChildren() {
        return children;
    }

    public ObjectEsParameter addChild(AbstractEsParameter child) {
        Objects.requireNonNull(child);

        children.add(child);
        return this;
    }

    @Override
    public String toString() {
        return getName();
    }

}
