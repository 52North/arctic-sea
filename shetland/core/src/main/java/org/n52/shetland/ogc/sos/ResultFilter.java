/*
 * Copyright (C) 2015-2021 52°North Spatial Information Research GmbH
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
package org.n52.shetland.ogc.sos;

import org.n52.shetland.ogc.filter.Filter;
import org.n52.shetland.ogc.ows.extension.Extension;

/**
 *
 * @author Carsten Hollmann
 * @since 4.4.1
 *
 */
public class ResultFilter implements Extension<Filter<?>> {

    private Filter<?> filter;
    private String namespace = ResultFilterConstants.NS_RF;

    public ResultFilter(Filter<?> filter) {
        setValue(filter);
    }

    public ResultFilter(Filter<?> filter, String namespace) {
        this(filter);
        setNamespace(namespace);
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public ResultFilter setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    @Override
    public boolean isSetNamespace() {
        return getNamespace() != null && !getNamespace().isEmpty();
    }

    @Override
    public String getIdentifier() {
        return ResultFilterConstants.RESULT_FILTER;
    }

    @Override
    public ResultFilter setIdentifier(String identifier) {
        return null;
    }

    @Override
    public boolean isSetIdentifier() {
        return true;
    }

    @Override
    public String getDefinition() {
        return ResultFilterConstants.RESULT_FILTER;
    }

    @Override
    public ResultFilter setDefinition(String definition) {
        return this;
    }

    @Override
    public boolean isSetDefinition() {
        return true;
    }

    @Override
    public Filter<?> getValue() {
        return filter;
    }

    @Override
    public ResultFilter setValue(Filter<?> value) {
        this.filter = value;
        return this;
    }

}
