/*
 * Copyright 2015 52°North Initiative for Geospatial Open Source
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.ogc.ows.OwsExtendedCapabilitiesProviderKey;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.util.activation.ActivationInitializer;
import org.n52.iceland.util.activation.ActivationListener;
import org.n52.iceland.util.activation.ActivationSource;
import org.n52.iceland.util.activation.DefaultActivationInitializer;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ActivationService {
    private static final Logger LOG = LoggerFactory
            .getLogger(ActivationService.class);

    private ActivationDao persistingActivationManagerDao;

    @Inject
    public void setPersistingActivationManagerDao(ActivationDao dao) {
        this.persistingActivationManagerDao = dao;
    }

    /**
     * Checks if the binding is active.
     *
     * @param key
     *            the binding
     *
     * @return if the binding is active
     *
     * @throws ConnectionProviderException
     */
    public boolean isBindingActive(BindingKey key)
            throws ConnectionProviderException {
        return persistingActivationManagerDao.isBindingActive(key);
    }

    /**
     * Checks if the extended capabilities is active.
     *
     * @param key
     *            the extended capabilities key
     *
     * @return if the extended capabilities is active
     *
     * @throws ConnectionProviderException
     */
    public boolean isOwsExtendedCapabilitiesProviderActive(
            OwsExtendedCapabilitiesProviderKey key)
            throws ConnectionProviderException {
        return persistingActivationManagerDao
                .isOwsExtendedCapabilitiesProviderActive(key);
    }

    /**
     * Returns if a operation is active and should be offered by this SOS.
     *
     * @param key
     *            the key identifying the operation
     *
     * @return {@code true} if the operation is active in this SOS
     *
     * @throws ConnectionProviderException
     */
    public boolean isRequestOperatorActive(RequestOperatorKey key)
            throws ConnectionProviderException {
        return persistingActivationManagerDao.isRequestOperatorActive(key);
    }

    public ActivationListener<RequestOperatorKey> getRequestOperatorListener() {
        return new AbstractActivationListener<RequestOperatorKey>() {
            @Override
            protected void setStatus(RequestOperatorKey key, boolean active)
                    throws ConnectionProviderException {
                persistingActivationManagerDao.setOperationStatus(key, active);
            }
        };
    }

    public ActivationSource<RequestOperatorKey> getRequestOperatorSource() {
        return new AbstractActivationSource<RequestOperatorKey>() {
            @Override
            protected boolean check(RequestOperatorKey key) throws ConnectionProviderException {
                return isRequestOperatorActive(key);
            }

            @Override
            protected Set<RequestOperatorKey> get() throws ConnectionProviderException {
                return persistingActivationManagerDao.getRequestOperatorKeys();
            }
        };
    }

    public ActivationInitializer<RequestOperatorKey> getRequestOperatorInitializer() {
        return new DefaultActivationInitializer<>(getRequestOperatorSource());
    }

    public ActivationListener<BindingKey> getBindingListener() {
        return new AbstractActivationListener<BindingKey>() {
            @Override
            protected void setStatus(BindingKey key, boolean active)
                    throws ConnectionProviderException {
                persistingActivationManagerDao.setBindingStatus(key, active);
            }
        };
    }

    public ActivationSource<BindingKey> getBindingSource() {
        return new AbstractActivationSource<BindingKey>() {
            @Override
            protected boolean check(BindingKey key)
                    throws ConnectionProviderException {
                return isBindingActive(key);
            }

            @Override
            protected Set<BindingKey> get()
                    throws ConnectionProviderException {
                return persistingActivationManagerDao.getBindingKeys();
            }
        };
    }

    public ActivationInitializer<BindingKey> getBindingInitializer() {
        return new DefaultActivationInitializer<>(getBindingSource());
    }

    public ActivationListener<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabiltiesListener() {
        return new AbstractActivationListener<OwsExtendedCapabilitiesProviderKey>() {
            @Override
            protected void setStatus(OwsExtendedCapabilitiesProviderKey key,
                                     boolean active)
                    throws ConnectionProviderException {
                persistingActivationManagerDao
                        .setOwsExtendedCapabilitiesStatus(key, active);
            }
        };

    }

    public ActivationSource<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabiltiesSource() {
        return new AbstractActivationSource<OwsExtendedCapabilitiesProviderKey>() {
            @Override
            protected boolean check(OwsExtendedCapabilitiesProviderKey key)
                    throws ConnectionProviderException {
                return isOwsExtendedCapabilitiesProviderActive(key);
            }

            @Override
            protected Set<OwsExtendedCapabilitiesProviderKey> get()
                    throws ConnectionProviderException {
                return persistingActivationManagerDao
                        .getOwsExtendedCapabilitiesProviderKeys();
            }
        };
    }
    public ActivationInitializer<OwsExtendedCapabilitiesProviderKey> getOwsExtendedCapabiltiesInitializer() {
        return new DefaultActivationInitializer<>(getOwsExtendedCapabiltiesSource());
    }

    protected static abstract class AbstractActivationSource<K> implements
            ActivationSource<K> {
        @Override
        public boolean isActive(K key) {
            try {
                return check(key);
            } catch (ConnectionProviderException ex) {
                throw new ConfigurationException(ex);
            }
        }

        @Override
        public Set<K> getKeys() {
            try {
                return get();
            } catch (ConnectionProviderException ex) {
                throw new ConfigurationException(ex);
            }
        }

        protected abstract Set<K> get()
                throws ConnectionProviderException;

        protected abstract boolean check(K key)
                throws ConnectionProviderException;
    }

    protected static abstract class AbstractActivationListener<K> implements
            ActivationListener<K> {
        @Override
        public void activated(K key) {
            set(key, true);
        }

        @Override
        public void deactivated(K key) {
            set(key, false);
        }

        private void set(K key, boolean status)
                throws ConfigurationException {
            try {
                LOG.debug("Setting status of {} to {}", key, status);
                setStatus(key, status);
            } catch (ConnectionProviderException ex) {
                throw new ConfigurationException(ex);
            }
        }

        protected abstract void setStatus(K key, boolean active)
                throws ConnectionProviderException;
    }

}
