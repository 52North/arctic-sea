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
package org.n52.shetland.ogc.sos.request;

import org.n52.shetland.ogc.filter.SpatialFilter;
import org.n52.shetland.ogc.sos.Sos2Constants;

/**
 * Interface to define methods for requests that supports SpatialFilteringProfile spatial filter
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
    default boolean isSetSpatialFilter() {
        return getSpatialFilter() != null;
    }

    /**
     * Check if SpatialFilter is a SpatialFilteringProfile spatial filter
     *
     * @return True if SpatialFilter is a SpatialFilteringProfile spatial filter
     */
    default boolean hasSpatialFilteringProfileSpatialFilter() {
        return isSetSpatialFilter() && getSpatialFilter().getValueReference()
                .equals(Sos2Constants.VALUE_REFERENCE_SPATIAL_FILTERING_PROFILE);
    }

}
