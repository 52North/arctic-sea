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
package org.n52.shetland.ogc.sos.response;

import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;

/**
 * Interface to indicate that the response may contain streaming data.
 *
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 4.3.0
 *
 */
public interface StreamingDataResponse {

    /**
     *  Check if the response contains streaming data {@link AbstractStreaming}
     *
     * @return <code>true</code> if the response contains streaming data
     */
    boolean hasStreamingData();

    /**
     * Merge the streaming data.
     *
     * @throws OwsExceptionReport If an error occurs.
     */
    void mergeStreamingData() throws OwsExceptionReport;
}
