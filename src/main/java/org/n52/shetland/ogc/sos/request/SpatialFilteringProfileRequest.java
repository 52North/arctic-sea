/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.shetland.ogc.sos.request;

import org.n52.shetland.ogc.filter.SpatialFilter;

/**
 * Interface to define methods for requests that supports
 * SpatialFilteringProfile spatial filter
 *
 * @since 1.0.0
 *
 */
public interface SpatialFilteringProfileRequest {

    /**
     * Get SpatialFilter
     *
     * @return SpatialFilter
     */
    SpatialFilter getSpatialFilter();

    /**
     * Set SpatialFilter
     *
     * @param spatialFilter
     *            SpatialFilter to set
     */
    void setSpatialFilter(SpatialFilter spatialFilter);

    /**
     * Check if SpatialFilter is set
     *
     * @return True if SpatialFilter is set
     */
    boolean isSetSpatialFilter();

    /**
     * Check if SpatialFilter is a SpatialFilteringProfile spatial filter
     *
     * @return True if SpatialFilter is a SpatialFilteringProfile spatial filter
     */
    boolean hasSpatialFilteringProfileSpatialFilter();

}
