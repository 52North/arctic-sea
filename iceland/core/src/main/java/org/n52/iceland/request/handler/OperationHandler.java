/*
 * Copyright 2015-${currentYear} 52°North Spatial Information Research GmbH
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
package org.n52.iceland.request.handler;

import org.n52.janmayen.component.Component;
import org.n52.shetland.ogc.ows.OwsOperation;
import org.n52.shetland.ogc.ows.exception.OwsExceptionReport;
import org.n52.svalbard.ConformanceClass;

/**
 * Interface for all operation Handlers.
 *
 * In 52N SOS version 4.x called OperationDAO
 *
 * @since 1.0.0
 */
public interface OperationHandler extends ConformanceClass, Component<OperationHandlerKey> {

    /**
     * TODO check if necessary in feature
     *
     * Get the operation name this Handler supports
     *
     * @return The supported operation name
     */
    String getOperationName();

    /**
     * Get the OperationsMetadata of the supported SOS operation for the
     * capabilities
     *
     * @param service
     *            OGC service identifier
     * @param version
     *            Service version
     * @return OperationsMetadata for the operation
     *
     * @throws OwsExceptionReport
     *             If an error occurs.
     */
    OwsOperation getOperationsMetadata(String service, String version) throws OwsExceptionReport;

    /**
     * Check if the operation and all necessary sources (e.g. tables) are available.
     *
     * @return {@code true}, if the operation is supported
     */
    default boolean isSupported() {
        return true;
    }
}
