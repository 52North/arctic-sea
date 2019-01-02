/*
 * Copyright 2015-2019 52°North Initiative for Geospatial Open Source
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

import javax.inject.Inject;

import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.ogc.ows.extension.OwsOperationMetadataExtensionProviderKey;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.util.activation.ActivationInitializer;
import org.n52.iceland.util.activation.ActivationSource;
import org.n52.iceland.util.activation.DefaultActivationInitializer;
import org.n52.iceland.util.activation.FunctionalActivationListener;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ActivationService {

    private ActivationDao activationDao;

    @Inject
    public void setActivationDao(ActivationDao dao) {
        this.activationDao = dao;
    }

    /**
     * @return the dao
     */
    protected ActivationDao getActivationDao() {
        return this.activationDao;
    }

    /**
     * Checks if the binding is active.
     *
     * @param key the binding
     *
     * @return if the binding is active
     */
    public boolean isBindingActive(BindingKey key) {
        return getActivationDao().isBindingActive(key);
    }

    /**
     * Checks if the extended capabilities is active.
     *
     * @param key the extended capabilities key
     *
     * @return if the extended capabilities is active
     *
     */
    public boolean isOwsOperationMetadataExtensionProviderActive(OwsOperationMetadataExtensionProviderKey key) {
        return this.getActivationDao().isOwsOperationMetadataExtensionProviderActive(key);
    }

    /**
     * Returns if a operation is active and should be offered by this service.
     *
     * @param key the key identifying the operation
     *
     * @return {@code true} if the operation is active in this service
     */
    public boolean isRequestOperatorActive(RequestOperatorKey key) {
        return getActivationDao().isRequestOperatorActive(key);
    }

    public FunctionalActivationListener<RequestOperatorKey> getRequestOperatorListener() {
        return getActivationDao()::setOperationStatus;
    }

    public ActivationSource<RequestOperatorKey> getRequestOperatorSource() {
        return ActivationSource.create(this::isRequestOperatorActive,
                                       this::getRequestOperatorKeys);
    }

    public Set<RequestOperatorKey> getRequestOperatorKeys() {
        return getActivationDao().getRequestOperatorKeys();
    }

    public ActivationInitializer<RequestOperatorKey> getRequestOperatorInitializer() {
        return new DefaultActivationInitializer<>(getRequestOperatorSource());
    }

    public FunctionalActivationListener<BindingKey> getBindingListener() {
        return getActivationDao()::setBindingStatus;
    }

    public ActivationSource<BindingKey> getBindingSource() {
        return ActivationSource.create(this::isBindingActive, this::getBindingKeys);
    }

    public Set<BindingKey> getBindingKeys() {
        return getActivationDao().getBindingKeys();
    }

    public ActivationInitializer<BindingKey> getBindingInitializer() {
        return new DefaultActivationInitializer<>(getBindingSource());
    }

    public FunctionalActivationListener<OwsOperationMetadataExtensionProviderKey>
            getOwsOperationMetadataExtensionProviderListener() {
        return this.getActivationDao()::setOwsOperationMetadataExtensionProviderStatus;
    }

    public ActivationSource<OwsOperationMetadataExtensionProviderKey>
            getOwsOperationMetadataExtensionProviderSource() {
        return ActivationSource.create(this::isOwsOperationMetadataExtensionProviderActive,
                                       this::getOwsOperationMetadataExtensionProviderKeys);
    }

    public Set<OwsOperationMetadataExtensionProviderKey> getOwsOperationMetadataExtensionProviderKeys() {
        return getActivationDao().getOwsOperationMetadataExtensionProviderKeys();
    }

    public ActivationInitializer<OwsOperationMetadataExtensionProviderKey>
            getOwsOperationMetadataExtensionProviderInitializer() {
        return new DefaultActivationInitializer<>(getOwsOperationMetadataExtensionProviderSource());
    }

}
