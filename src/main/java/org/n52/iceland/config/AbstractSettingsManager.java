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

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import org.n52.iceland.binding.BindingKey;
import org.n52.iceland.binding.BindingRepository;
import org.n52.iceland.coding.CodingRepository;
import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.encode.ProcedureDescriptionFormatKey;
import org.n52.iceland.encode.ResponseFormatKey;
import org.n52.iceland.event.ServiceEventBus;
import org.n52.iceland.event.events.SettingsChangeEvent;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.ogc.ows.OwsExtendedCapabilitiesProviderKey;
import org.n52.iceland.ogc.ows.OwsExtendedCapabilitiesProviderRepository;
import org.n52.iceland.ogc.swes.OfferingExtensionKey;
import org.n52.iceland.ogc.swes.OfferingExtensionRepository;
import org.n52.iceland.request.operator.RequestOperatorKey;
import org.n52.iceland.request.operator.RequestOperatorRepository;
import org.n52.iceland.service.ServiceSettings;
import org.n52.iceland.util.collections.HashSetMultiMap;
import org.n52.iceland.util.collections.SetMultiMap;
import org.n52.iceland.lifecycle.Constructable;

/**
 * Abstract {@code SettingsManaeger} implementation that handles the loading of
 * {@link SettingDefinition}s and the configuration of objects.
 * <p/>
 *
 * @author Christian Autermann <c.autermann@52north.org>
 * @since 4.0.0
 */
public abstract class AbstractSettingsManager extends SettingsManager implements Constructable{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSettingsManager.class);
    private final SetMultiMap<String, ConfigurableObject> configurableObjects = new HashSetMultiMap<>();
    private final ReadWriteLock configurableObjectsLock = new ReentrantReadWriteLock();
    @Inject
    private RequestOperatorRepository requestOperatorRepository;
    @Inject
    private CodingRepository codingRepository;
    @Inject
    private BindingRepository bindingRepository;
    @Inject
    private OfferingExtensionRepository offeringExtensionRepository;
    @Inject
    private OwsExtendedCapabilitiesProviderRepository owsExtendedCapabilitiesRepository;
    @Inject
    private ApplicationContext applicationContext;
    private Set<SettingDefinition<?,?>> definitions;
    private Map<String, SettingDefinition<?,?>> definitionByKey;

    @Override
    public void init() {
        Collection<SettingDefinition> loaded = loadSettingDefinitions();
        this.definitions = new HashSet<>(loaded.size());
        this.definitionByKey = new HashMap<>(loaded.size());
        for (SettingDefinition<?,?> definition : this.definitions) {
            this.definitions.add(definition);
            if (this.definitionByKey.put(definition.getKey(), definition) != null) {
                LOG.warn("Duplicate setting definition for key {}", definition.getKey());
            }
        }
    }

    private Collection<SettingDefinition> loadSettingDefinitions() {
        return this.applicationContext.getBeansOfType(SettingDefinition.class).values();
    }

    @Override
    public Set<SettingDefinition<?, ?>> getSettingDefinitions() {
        return Collections.unmodifiableSet(this.definitions);
    }

    @Override
    public SettingDefinition<?, ?> getDefinitionByKey(String key) {
        return this.definitionByKey.get(key);
    }

    /**
     * @return the keys for all definiions
     */
    public Set<String> getKeys() {
        Set<SettingDefinition<?, ?>> settings = getSettingDefinitions();
        HashSet<String> keys = new HashSet<>(settings.size());
        for (SettingDefinition<?, ?> setting : settings) {
            keys.add(setting.getKey());
        }
        return keys;
    }

    @Override
    public void changeSetting(SettingValue<?> newValue) throws ConfigurationException, ConnectionProviderException {
        if (newValue == null) {
            throw new NullPointerException("newValue can not be null");
        }
        if (newValue.getKey() == null) {
            throw new NullPointerException("newValue.key can not be null");
        }
        SettingDefinition<?, ?> def = getDefinitionByKey(newValue.getKey());

        if (def == null) {
            throw new IllegalArgumentException("newValue.key is invalid");
        }

        if (def.getType() != newValue.getType()) {
            throw new IllegalArgumentException(String.format("Invalid type for definition (%s vs. %s)", def.getType(),
                    newValue.getType()));
        }

        SettingValue<?> oldValue = getSettingValue(newValue.getKey());
        if (oldValue == null || !oldValue.equals(newValue)) {
            applySetting(def, oldValue, newValue);
            saveSettingValue(newValue);
            ServiceEventBus.fire(new SettingsChangeEvent(def, oldValue, newValue));
        }
    }

    @Override
    public void deleteSetting(SettingDefinition<?, ?> setting) throws ConfigurationException,
            ConnectionProviderException {
        SettingValue<?> oldValue = getSettingValue(setting.getKey());
        if (oldValue != null) {
            applySetting(setting, oldValue, null);
            deleteSettingValue(setting.getKey());
            ServiceEventBus.fire(new SettingsChangeEvent(setting, oldValue, null));
        }
    }

    /**
     * Applies the a new setting to all {@code ConfiguredObject}s. If an error
     * occurs the the old value is reapplied.
     * <p/>
     *
     * @param setting
     *            the definition
     * @param oldValue
     *            the old value (or {@code null} if there is none)
     * @param newValue
     *            the new value (or {@code null} if there is none)
     *            <p/>
     * @throws ConfigurationException
     *             if there is a error configuring the objects
     */
    private void applySetting(SettingDefinition<?, ?> setting, SettingValue<?> oldValue, SettingValue<?> newValue)
            throws ConfigurationException {
        LinkedList<ConfigurableObject> changed = new LinkedList<>();
        ConfigurationException e = null;
        configurableObjectsLock.readLock().lock();
        try {
            Set<ConfigurableObject> cos = configurableObjects.get(setting.getKey());
            if (cos != null) {
                for (ConfigurableObject co : cos) {
                    try {
                        co.configure(newValue.getValue());
                    } catch (ConfigurationException ce) {
                        e = ce;
                        break;
                    } finally {
                        changed.add(co);
                    }
                }
                if (e != null) {
                    LOG.debug("Reverting setting...");
                    for (ConfigurableObject co : changed) {
                        try {
                            co.configure(oldValue.getValue());
                        } catch (ConfigurationException ce) {
                            /* there is nothing we can do... */
                            LOG.error("Error reverting setting!", ce);
                        }
                    }
                    throw e;
                }
            }
        } finally {
            configurableObjectsLock.readLock().unlock();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> SettingValue<T> getSetting(SettingDefinition<?, T> key) throws ConnectionProviderException {
        return (SettingValue<T>) getSettingValue(key.getKey());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> SettingValue<T> getSetting(String key)
            throws ConnectionProviderException {
        SettingDefinition<?, ?> def = getDefinitionByKey(key);
        if (def == null) {
            return null;
        }
        return (SettingValue<T>) getSetting(def);
    }


    @Override
    public Map<SettingDefinition<?, ?>, SettingValue<?>> getSettings() throws ConnectionProviderException {
        Set<SettingValue<?>> values = getSettingValues();
        Map<SettingDefinition<?, ?>, SettingValue<?>> settingsByDefinition = new HashMap<>(values.size());
        for (SettingValue<?> value : values) {
            final SettingDefinition<?, ?> definition = getDefinitionByKey(value.getKey());
            if (definition == null) {
                LOG.warn("No definition for '{}' found.", value.getKey());
            } else {
                settingsByDefinition.put(definition, value);
            }
        }
        HashSet<SettingDefinition<?, ?>> nullValues = new HashSet<>(getSettingDefinitions());
        nullValues.removeAll(settingsByDefinition.keySet());
        for (SettingDefinition<?, ?> s : nullValues) {
            settingsByDefinition.put(s, null);
        }
        return settingsByDefinition;
    }

    @Override
    public void deleteAdminUser(AdministratorUser user) throws ConnectionProviderException {
        deleteAdminUser(user.getUsername());
    }

    @Override
    public boolean hasAdminUser() throws ConnectionProviderException {
        return !getAdminUsers().isEmpty();
    }


    @Override
    public void configure(Object object) throws ConfigurationException {
        LOG.debug("Configuring {}", object);
        Class<?> clazz = object.getClass();
        Configurable configurable = clazz.getAnnotation(Configurable.class);
        if (configurable == null) {
            return;
        }

        for (Method method : clazz.getMethods()) {
            Setting s = method.getAnnotation(Setting.class);

            if (s != null) {
                String key = s.value();
                if (key == null || key.isEmpty()) {
                    throw new ConfigurationException(String.format("Invalid value for @Setting: '%s'", key));
                }
                if (getDefinitionByKey(key) == null) {
                    throw new ConfigurationException(String.format("No SettingDefinition found for key %s", key));
                }
                if (method.getParameterTypes().length != 1) {
                    throw new ConfigurationException(String.format(
                            "Method %s annotated with @Setting in %s has a invalid method signature", method, clazz));
                }
                if (!Modifier.isPublic(method.getModifiers())) {
                    throw new ConfigurationException(String.format(
                            "Non-public method %s annotated with @Setting in %s", method, clazz));
                }
                configure(new ConfigurableObject(method, object, key));
            }
        }
    }

    private void configure(ConfigurableObject co) throws ConfigurationException {
        LOG.debug("Configuring {}", co);
        configurableObjectsLock.writeLock().lock();
        try {
            configurableObjects.add(co.getKey(), co);
        } finally {
            configurableObjectsLock.writeLock().unlock();
        }
        try {
            co.configure(getNotNullSettingValue(co));
        } catch (ConnectionProviderException | RuntimeException cpe) {
            throw new ConfigurationException("Exception configuring " + co.getKey(), cpe);
        }

    }

    @SuppressWarnings("unchecked")
    private SettingValue<Object> getNotNullSettingValue(ConfigurableObject co) throws ConnectionProviderException,
            ConfigurationException {
        SettingValue<Object> val = (SettingValue<Object>) getSettingValue(co.getKey());
        if (val == null) {
            SettingDefinition<?, ?> def = getDefinitionByKey(co.getKey());
            if (def == null) {
                throw new ConfigurationException(String.format("No SettingDefinition found for key %s", co.getKey()));
            }
            val = (SettingValue<Object>) getSettingFactory().newSettingValue(def, null);
            if (def.isOptional()) {
                LOG.debug("No value found for optional setting {}", co.getKey());
                saveSettingValue(val);
            } else if (def.hasDefaultValue()) {
                LOG.debug("Using default value '{}' for required setting {}", def.getDefaultValue(), co.getKey());
                saveSettingValue(val.setValue(def.getDefaultValue()));
            } else if (def.getKey().equals(ServiceSettings.SERVICE_URL)) {
                saveSettingValue(val.setValue(URI.create("http://localhost:8080/iceland/service")));
            } else {
                throw new ConfigurationException(String.format(
                        "No value found for required Setting '%s' with no default value.", co.getKey()));
            }
        }
        return val;
    }

    @Override
    public void setActive(RequestOperatorKey rokt, boolean active) throws ConnectionProviderException {
        LOG.debug("Setting status of {} to {}", rokt, active);
        setOperationStatus(rokt, active);
        this.requestOperatorRepository.setActive(rokt, active);
    }

    @Override
    public void setActive(ResponseFormatKey rfkt, boolean active) throws ConnectionProviderException {
        LOG.debug("Setting status of {} to {}", rfkt, active);
        setResponseFormatStatus(rfkt, active);
        this.codingRepository.setActive(rfkt, active);
    }

    @Override
    public void setActive(ProcedureDescriptionFormatKey pdfkt, boolean active) throws ConnectionProviderException {
        LOG.debug("Setting status of {} to {}", pdfkt, active);
        setProcedureDescriptionFormatStatus(pdfkt, active);
        this.codingRepository.setActive(pdfkt, active);
    }

    @Override
    public void setActive(BindingKey bk, boolean active) throws ConnectionProviderException {
        LOG.debug("Setting status of {} to {}", bk, active);
        setBindingStatus(bk, active);
        this.bindingRepository.setActive(bk, active);
    }

    @Override
    public void setActive(OfferingExtensionKey oek, boolean active) throws ConnectionProviderException {
        setActive(oek, active, true);
    }

    @Override
    public void setActive(OfferingExtensionKey oek, boolean active, boolean updateRepository) throws ConnectionProviderException {
        LOG.debug("Setting status of {} to {}", oek, active);
        setOfferingExtensionStatus(oek, active);
        this.offeringExtensionRepository.setActive(oek, active);
    }

    @Override
    public void setActive(OwsExtendedCapabilitiesProviderKey oeck, boolean active) throws ConnectionProviderException {
        setActive(oeck, active, true);
    }

    @Override
    public void setActive(OwsExtendedCapabilitiesProviderKey oeck, boolean active, boolean updateRepository) throws ConnectionProviderException {
        LOG.debug("Setting status of {} to {}", oeck, active);
        setOwsExtendedCapabilitiesStatus(oeck, active);
        this.owsExtendedCapabilitiesRepository.setActive(oeck, active);
    }

    /**
     * @return all saved setting values
     *
     * @throws ConnectionProviderException
     */
    protected abstract Set<SettingValue<?>> getSettingValues() throws ConnectionProviderException;

    /**
     * Returns the value of the specified setting or {@code null} if it does not
     * exist.
     * <p/>
     *
     * @param key
     *            the key
     *            <p/>
     * @return the value
     *
     * @throws ConnectionProviderException
     */
    protected abstract SettingValue<?> getSettingValue(String key) throws ConnectionProviderException;

    /**
     * Deletes the setting with the specified key.
     * <p/>
     *
     * @param key
     *            the key
     *
     * @throws ConnectionProviderException
     */
    protected abstract void deleteSettingValue(String key) throws ConnectionProviderException;

    /**
     * Saves the setting value.
     * <p/>
     *
     * @param setting
     *            the value
     *
     * @throws ConnectionProviderException
     */
    protected abstract void saveSettingValue(SettingValue<?> setting) throws ConnectionProviderException;

    /**
     * Sets the status of an operation.
     * <p/>
     *
     * @param requestOperatorKeyType
     *            the key identifying the operation
     * @param active
     *            whether the operation is active or not
     *            <p/>
     * @throws ConnectionProviderException
     * @see #setActive(RequestOperatorKey, boolean)
     */
    protected abstract void setOperationStatus(RequestOperatorKey requestOperatorKeyType, boolean active)
            throws ConnectionProviderException;

    /**
     * Sets the status of a response format for the specified service and
     * version.
     *
     * @param rfkt
     *            the service/version/responseFormat combination
     * @param active
     *            the status
     *
     * @throws ConnectionProviderException
     * @see #setActive(ResponseFormatKey, boolean)
     */
    protected abstract void setResponseFormatStatus(ResponseFormatKey rfkt, boolean active)
            throws ConnectionProviderException;

    /**
     * Sets the status of a response format for the specified service and
     * version.
     *
     * @param pdfkt
     *            the service/version/responseFormat combination
     * @param active
     *            the status
     *
     * @throws ConnectionProviderException
     * @see #setActive(ProcedureDescriptionFormatKey, boolean)
     */
    protected abstract void setProcedureDescriptionFormatStatus(ProcedureDescriptionFormatKey pdfkt, boolean active)
            throws ConnectionProviderException;

    /**
     * Sets the status of a binding.
     *
     * @param bk
     *            the binding
     * @param active
     *            the status
     *
     * @throws ConnectionProviderException
     * @see #setActive(org.n52.iceland.binding.BindingKey, boolean)
     */
    protected abstract void setBindingStatus(BindingKey bk, boolean active) throws ConnectionProviderException;

    protected abstract void setOfferingExtensionStatus(OfferingExtensionKey oek, boolean active) throws ConnectionProviderException;

    protected abstract void setOwsExtendedCapabilitiesStatus(OwsExtendedCapabilitiesProviderKey oeck, boolean active) throws ConnectionProviderException;

    private class ConfigurableObject {
        private final Method method;

        private final WeakReference<Object> target;

        private final String key;

        /**
         * Constructs a new {@code ConfigurableObject}
         * <p/>
         *
         * @param method
         *            the method of the target
         * @param target
         *            the target object
         * @param key
         *            the settings key
         */
        ConfigurableObject(Method method, Object target, String key) {
            this.method = method;
            this.target = new WeakReference<>(target);
            this.key = key;
        }

        /**
         * @return the method
         */
        public Method getMethod() {
            return method;
        }

        /**
         * @return the target object
         */
        public WeakReference<Object> getTarget() {
            return target;
        }

        /**
         * @return the settings key
         */
        public String getKey() {
            return key;
        }

        /**
         * Configures this object with the specified value.
         * <p/>
         *
         * @param val
         *            the value
         *            <p/>
         * @throws ConfigurationException
         *             if an error occurs
         */
        public void configure(SettingValue<?> val) throws ConfigurationException {
            configure(val.getValue());
        }

        /**
         * Configures this object with the specified value. Exceptions are
         * wrapped in a {@code ConfigurationException}.
         * <p/>
         *
         * @param val
         *            the value
         *            <p/>
         * @throws ConfigurationException
         *             if an error occurs
         */
        public void configure(Object val) throws ConfigurationException {

            try {
                if (getTarget().get() != null) {
                    LOG.debug("Setting value '{}' for {}", val, this);
                    getMethod().invoke(getTarget().get(), val);
                }
            } catch (IllegalAccessException | IllegalArgumentException ex) {
                logAndThrowError(val, ex);
            } catch (InvocationTargetException ex) {
                logAndThrowError(val, ex.getTargetException());
            }
        }

        private void logAndThrowError(Object val, Throwable t) throws ConfigurationException {
            String message =
                    String.format("Error while setting value '%s' (%s) for property '%s' with method '%s'", val,
                            val == null ? null : val.getClass(), getKey(), getMethod());
            LOG.error(message);
            throw new ConfigurationException(message, t);
        }

        @Override
        public String toString() {
            return String.format("ConfigurableObject[key=%s, method=%s, target=%s]", getKey(), getMethod(),
                    getTarget().get());
        }

        @Override
        public int hashCode() {
            final int prime = 45;
            int hash = 5;
            hash = prime * hash + (getMethod() != null ? getMethod().hashCode() : 0);
            hash = prime * hash + (getTarget() != null ? getTarget().hashCode() : 0);
            hash = prime * hash + (getKey() != null ? getKey().hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ConfigurableObject other = (ConfigurableObject) obj;
            if (getMethod() != other.getMethod() && (getMethod() == null || !getMethod().equals(other.getMethod()))) {
                return false;
            }
            if (getTarget() != other.getTarget() && (getTarget() == null || !getTarget().equals(other.getTarget()))) {
                return false;
            }
            return !((getKey() == null) ? (other.getKey() != null) : !getKey().equals(other.getKey()));
        }
    }
}
