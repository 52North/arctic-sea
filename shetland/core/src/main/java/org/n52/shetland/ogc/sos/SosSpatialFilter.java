/*
 * Copyright 2015-2022 52°North Spatial Information Research GmbH
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

import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.ows.extension.Extension;

/**
 *
 * @author Carsten Hollmann
 * @since 4.4.1
 *
 */
public class SosSpatialFilter implements Extension<SpatialFilter> {

    private SpatialFilter filter;

    public SosSpatialFilter(SpatialFilter filter) {
        setValue(filter);
    }

    @Override
    public String getNamespace() {
        return SosSpatialFilterConstants.NS_SF;
    }

    @Override
    public SosSpatialFilter setNamespace(String namespace) {
        return this;
    }

    @Override
    public boolean isSetNamespace() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return SosSpatialFilterConstants.SPATIAL_FILTER;
    }

    @Override
    public SosSpatialFilter setIdentifier(String identifier) {
        return null;
    }

    @Override
    public boolean isSetIdentifier() {
        return true;
    }

    @Override
    public String getDefinition() {
        return SosSpatialFilterConstants.SPATIAL_FILTER;
    }

    @Override
    public SosSpatialFilter setDefinition(String definition) {
        return this;
    }

    @Override
    public boolean isSetDefinition() {
        return true;
    }

    @Override
    public SpatialFilter getValue() {
        return filter;
    }

    @Override
    public SosSpatialFilter setValue(SpatialFilter value) {
        this.filter = value;
        return this;
    }

}
