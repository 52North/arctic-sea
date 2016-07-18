/*
 * Copyright 2015-2016 52°North Initiative for Geospatial Open Source
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
import org.n52.iceland.ogc.ows.extension.OwsExtendedCapabilitiesProviderKey;
import org.n52.iceland.ogc.swes.OfferingExtensionKey;
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

    private ActivationDao dao;

    @Inject
    public void setPersistingActivationManagerDao(ActivationDao dao) {
        this.dao = dao;
    }

    /**
     * Checks if the binding is active.
     *
     * @param key
     *            the binding
     *
     * @return if the binding is active
     */
    public boolean isBindingActive(BindingKey key) {
        return this.dao.isBindingActive(key);
    }

    /**
     * Checks if the extended capabilities is active.
     *
     * @param key
     *            the extended capabilities key
     *
     * @return if the extended capabilities is active
     *
     */
    public boolean isOwsExtendedCapabilitiesProviderActive(
            OwsExtendedCapabilitiesProviderKey key) {
        return this.dao.isOwsExtendedCapabilitiesProviderActive(key);
    }

    /**
     * Returns if a operation is active and should be offered by this service.
     *
     * @param key
     *            the key identifying the operation
     *
     * @return {@code true} if the operation is active in this service
     */
    public boolean isRequestOperatorActive(RequestOperatorKey key) {
        return this.dao.isRequestOperatorActive(key);
    }

    public FunctionalActivationListener<RequestOperatorKey> getRequestOperatorListener() {
        return this.dao::setOperationStatus;
    }

    public ActivationSource<RequestOperatorKey> getRequestOperatorSource() {
        return ActivationSource.create(this::isRequestOperatorActive,
                                       this::getRequestOperatorKeys);
    }

    public Set<RequestOperatorKey> getRequestOperatorKeys() {
        return this.dao.getRequestOperatorKeys();
    }

    public ActivationInitializer<RequestOperatorKey> getRequestOperatorInitializer() {
        return new DefaultActivationInitializer<>(getRequestOperatorSource());
    }

    public FunctionalActivationListener<BindingKey> getBindingListener() {
        return this.dao::setBindingStatus;
    }

    public ActivationSource<BindingKey> getBindingSource() {
        return ActivationSource.create(this::isBindingActive,
                                       this::getBindingKeys);
    }

    public Set<BindingKey> getBindingKeys() {
        return this.dao.getBindingKeys();
    }

    public ActivationInitializer<BindingKey> getBindingInitializer() {
        return new DefaultActivationInitializer<>(getBindingSource());
    }

    public FunctionalActivationListener<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabiltiesListener() {
        return this.dao::setOwsExtendedCapabilitiesStatus;
    }

    public ActivationSource<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabiltiesSource() {
        return ActivationSource
                .create(this::isOwsExtendedCapabilitiesProviderActive,
                        this::getOwsExtendedCapabilitiesProviderKeys);
    }

    public Set<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabilitiesProviderKeys() {
        return this.dao.getOwsExtendedCapabilitiesProviderKeys();
    }

    public ActivationInitializer<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabiltiesInitializer() {
        return new DefaultActivationInitializer<>(getOwsExtendedCapabiltiesSource());
    }

    /**
     * Checks if the offering extension is active.
     *
     * @param key
     *            the offering extension key
     *
     * @return if the offering extension is active
     */
    public boolean isOfferingExtensionActive(OfferingExtensionKey key) {
        return this.dao.isOfferingExtensionActive(key);
    }

    public FunctionalActivationListener<OfferingExtensionKey> getOfferingExtensionListener() {
        return this.dao::setOfferingExtensionStatus;
    }

    public ActivationSource<OfferingExtensionKey> getOfferingExtensionSource() {
        return ActivationSource.create(this::isOfferingExtensionActive,
                                       this::getOfferingExtensionKeys);
    }

    protected Set<OfferingExtensionKey> getOfferingExtensionKeys() {
        return this.dao.getOfferingExtensionKeys();
    }

    public ActivationInitializer<OfferingExtensionKey> getOfferingExtensionInitializer() {
        return new DefaultActivationInitializer<>(getOfferingExtensionSource());
    }
}
