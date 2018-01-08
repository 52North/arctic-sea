/*
 * Copyright 2015-2018 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos;

import org.n52.shetland.ogc.filter.ComparisonFilter;
import org.n52.shetland.ogc.ows.extension.Extension;

/**
 *
 * @author Carsten Hollmann
 * @since 4.4.1
 *
 */
public class ResultFilter implements Extension<ComparisonFilter> {

    private ComparisonFilter filter;

    public ResultFilter(ComparisonFilter filter) {
        setValue(filter);
    }

    @Override
    public String getNamespace() {
        return ResultFilterConstants.NS_RF;
    }

    @Override
    public ResultFilter setNamespace(String namespace) {
        return this;
    }

    @Override
    public boolean isSetNamespace() {
        return true;
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
    public ComparisonFilter getValue() {
        return filter;
    }

    @Override
    public ResultFilter setValue(ComparisonFilter value) {
        this.filter = value;
        return this;
    }

}
