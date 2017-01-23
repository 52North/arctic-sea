/*
 * Copyright 2015-2017 52°North Initiative for Geospatial Open Source
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
package org.n52.iceland.config;

import java.util.Set;

import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.ogc.ows.extension.OwsOperationMetadataExtensionProviderKey;
import org.n52.iceland.request.operator.RequestOperatorKey;

/**
 *
 * @author Christian Autermann
 */
public interface ActivationDao {

    /**
     * Returns if a operation is active and should be offered by this service.
     *
     * @param key the key identifying the operation
     *
     * @return {@code true} if the operation is active in this service
     */
    public abstract boolean isRequestOperatorActive(RequestOperatorKey key);

    /**
     * Sets the status of an operation.
     *
     * @param key    the key identifying the operation
     * @param active whether the operation is active or not
     */
    void setOperationStatus(RequestOperatorKey key, boolean active);

    Set<RequestOperatorKey> getRequestOperatorKeys();

    /**
     * Checks if the binding is active.
     *
     * @param key the binding
     *
     * @return if the binding is active
     */
    public abstract boolean isBindingActive(BindingKey key);

    /**
     * Sets the status of a binding.
     *
     * @param key    the binding
     * @param active the status
     */
    void setBindingStatus(BindingKey key, boolean active);

    Set<BindingKey> getBindingKeys();

    /**
     * Checks if the extended capabilities is active.
     *
     * @param key the extended capabilities key
     *
     * @return if the extended capabilities is active
     */
    boolean isOwsOperationMetadataExtensionProviderActive(OwsOperationMetadataExtensionProviderKey key);

    void setOwsOperationMetadataExtensionProviderStatus(OwsOperationMetadataExtensionProviderKey key, boolean active);

    Set<OwsOperationMetadataExtensionProviderKey> getOwsOperationMetadataExtensionProviderKeys();
}
