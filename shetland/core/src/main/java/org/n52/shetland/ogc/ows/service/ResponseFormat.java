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
package org.n52.shetland.ogc.ows.service;

import com.google.common.base.Strings;

/**
 * Marker interface to responseFormat
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public interface ResponseFormat {

    /**
     * Get response format
     *
     * @return response format
     */
    String getResponseFormat();

    /**
     * Set response format
     *
     * @param responseFormat
     *            response format
     */
    void setResponseFormat(String responseFormat);

    /**
     * Is response format set?
     *
     * @return True if response format is set
     */
    default boolean isSetResponseFormat() {
        return !Strings.isNullOrEmpty(getResponseFormat());
    }

}
