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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.iceland.config.annotation.Configurable;
import org.n52.iceland.config.annotation.Setting;
import org.n52.iceland.ds.ConnectionProviderException;
import org.n52.iceland.event.ServiceEventBus;
import org.n52.iceland.event.events.SettingsChangeEvent;
import org.n52.iceland.exception.ConfigurationException;
import org.n52.iceland.service.ServiceSettings;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * Class to handle the settings and configuration of the SOS. Allows other
 * classes to change, delete, and declare settings and to create, modify and
 * delete administrator users. Classes can subscribe to specific settings using
 * the {@code Configurable} and {@code Setting} annotations. To be recognized by
 * the SettingsManager {@link #configure(java.lang.Object)} has to be called for
 * every object that wants to receive settings. All other classes have to call
 * {@code configure(java.lang.Object)} manually.
 *
 *
 * @see AdministratorUser
 * @see SettingDefinition
 * @see SettingDefinitionProvider
 * @see SettingValue
 * @see Configurable
 * @author Christian Autermann <c.autermann@52north.org>
 * @since 4.0.0
 */
public class SettingsManager implements AdminUserService {
    private static final Logger LOG = LoggerFactory .getLogger(SettingsManager.class);
    private final SetMultimap<String, ConfigurableObject> configurableObjects = HashMultimap.create();
    private final ReadWriteLock configurableObjectsLock = new ReentrantReadWriteLock();
    private Set<SettingDefinition<?, ?>> definitions;
    private Map<String, SettingDefinition<?, ?>> definitionByKey;
    private SettingsManagerDao settingsManagerDao;
    private AdminUserDao adminUserDao;
    private SettingValueFactory settingValueFactory;
    private ServiceEventBus serviceEventBus;

    @Inject
    public void setServiceEventBus(ServiceEventBus serviceEventBus) {
        this.serviceEventBus = serviceEventBus;
    }

    @Inject
    public void setSettingValueFactory(SettingValueFactory settingValueFactory) {
        this.settingValueFactory = settingValueFactory;
    }

    @Inject
    public void setSettingsManagerDao(SettingsManagerDao settingsManagerDao) {
        this.settingsManagerDao = settingsManagerDao;
    }

    @Inject
    public void setAdminUserDao(AdminUserDao adminUserDao) {
        this.adminUserDao = adminUserDao;
    }

    @Inject
    public void setSettingDefinitions(Collection<SettingDefinition> definitions) {
        this.definitions = new HashSet<>(definitions.size());
        this.definitionByKey = new HashMap<>(definitions.size());
        for (SettingDefinition<?, ?> definition : definitions) {
            this.definitions.add(definition);
            if (this.definitionByKey.put(definition.getKey(), definition) != null) {
                LOG.warn("Duplicate setting definition for key {}", definition.getKey());
            }
        }
    }

    /**
     * Gets all {@code SettingDefinition}s known by this class.
     *
     * @return the definitions
     */
    public Set<SettingDefinition<?, ?>> getSettingDefinitions() {
        return Collections.unmodifiableSet(this.definitions);
    }

    /**
     * Configure {@code o} with the required settings. All changes to a setting
     * required by the object will be applied.
     *
     * @param object
     *          the object to configure
     *
     * @throws ConfigurationException
     *                                if there is a problem configuring the object
     * @see Configurable
     * @see Setting
     */
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
                } else if (getDefinitionByKey(key) == null) {
                    throw new ConfigurationException(String.format("No SettingDefinition found for key %s", key));
                } else if (method.getParameterTypes().length != 1) {
                    throw new ConfigurationException(String.format(
                            "Method %s annotated with @Setting in %s has a invalid method signature", method, clazz));
                } else if (!Modifier.isPublic(method.getModifiers())) {
                    throw new ConfigurationException(String.format(
                            "Non-public method %s annotated with @Setting in %s", method, clazz));
                } else {
                    configure(new ConfigurableObject(method, object, key));
                }
            }
        }
    }

    /**
     * Get the definition that is defined with the specified key.
     *
     * @param key
     *            the key
     *
     * @return the definition or {@code null} if there is no definition for the
     *         key
     */
    public SettingDefinition<?, ?> getDefinitionByKey(String key) {
        return this.definitionByKey.get(key);
    }

    /**
     * Gets the value of the setting defined by {@code key}.
     *
     * @param <T>
     *            the type of the setting and value
     * @param key
     *            the definition of the setting
     *
     * @return the value of the setting
     *
     * @throws ConnectionProviderException
     */
    @SuppressWarnings("unchecked")
    public <T> SettingValue<T> getSetting(SettingDefinition<?, T> key) throws ConnectionProviderException {
        return (SettingValue<T>) this.settingsManagerDao.getSettingValue(key.getKey());
    }

    /**
     * Gets the value of the setting defined by {@code key}.
     *
     * @param <T>
     *            the type of the setting and value
     * @param key
     *            the id of the setting
     *
     * @return the value of the setting
     *
     * @throws ConnectionProviderException
     */
    @SuppressWarnings("unchecked")
    public <T> SettingValue<T> getSetting(String key) throws ConnectionProviderException {
        SettingDefinition<?, ?> def = getDefinitionByKey(key);
        if (def == null) {
            return null;
        }
        return (SettingValue<T>) getSetting(def);
    }

    /**
     * Gets all values for all definitions. If there is no value for a
     * definition {@code null} is added to the map.
     *
     * @return all values by definition
     *
     * @throws ConnectionProviderException
     */
    public  Map<SettingDefinition<?, ?>, SettingValue<?>> getSettings()
            throws ConnectionProviderException {
        Set<SettingValue<?>> values = this.settingsManagerDao.getSettingValues();
        Map<SettingDefinition<?, ?>, SettingValue<?>> settingsByDefinition
                = new HashMap<>(values.size());
        for (SettingValue<?> value : values) {
            final SettingDefinition<?, ?> definition = getDefinitionByKey(value
                    .getKey());
            if (definition == null) {
                LOG.warn("No definition for '{}' found.", value.getKey());
            } else {
                settingsByDefinition.put(definition, value);
            }
        }
        HashSet<SettingDefinition<?, ?>> nullValues
                = new HashSet<>(getSettingDefinitions());
        nullValues.removeAll(settingsByDefinition.keySet());
        for (SettingDefinition<?, ?> s : nullValues) {
            settingsByDefinition.put(s, null);
        }
        return settingsByDefinition;
    }

    /**
     * Deletes the setting defined by {@code setting}.
     *
     * @param setting
     *                the definition
     *
     * @throws ConfigurationException
     *                                     if there is a problem deleting the setting
     * @throws ConnectionProviderException
     */
    public  void deleteSetting(SettingDefinition<?, ?> setting)
            throws ConfigurationException, ConnectionProviderException {
        SettingValue<?> oldValue = this.settingsManagerDao.getSettingValue(setting.getKey());
        if (oldValue != null) {
            applySetting(setting, oldValue, null);
            this.settingsManagerDao.deleteSettingValue(setting.getKey());
            this.serviceEventBus.submit(new SettingsChangeEvent(setting, oldValue, null));
        }
    }

    /**
     * @return the keys for all definitions
     */
    public Set<String> getKeys() {
        Set<SettingDefinition<?, ?>> settings = getSettingDefinitions();
        HashSet<String> keys = new HashSet<>(settings.size());
        for (SettingDefinition<?, ?> setting : settings) {
            keys.add(setting.getKey());
        }
        return keys;
    }

    /**
     * Applies the a new setting to all {@code ConfiguredObject}s. If an error
     * occurs the the old value is reapplied.
     * <p/>
     *
     * @param setting
     *                 the definition
     * @param oldValue
     *                 the old value (or {@code null} if there is none)
     * @param newValue
     *                 the new value (or {@code null} if there is none)
     * <p/>
     * @throws ConfigurationException
     *                                if there is a error configurin(g the objects
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

    private void configure(ConfigurableObject co) throws ConfigurationException {
        LOG.debug("Configuring {}", co);
        this.configurableObjectsLock.writeLock().lock();
        try {
            this.configurableObjects.put(co.getKey(), co);
        } finally {
            this.configurableObjectsLock.writeLock().unlock();
        }
        try {
            co.configure(getNotNullSettingValue(co));
        } catch (ConnectionProviderException | RuntimeException cpe) {
            throw new ConfigurationException("Exception configuring " + co.getKey(), cpe);
        }
    }

    @SuppressWarnings("unchecked")
    private SettingValue<Object> getNotNullSettingValue(ConfigurableObject co)
            throws ConnectionProviderException, ConfigurationException {
        SettingValue<Object> val = (SettingValue<Object>) this.settingsManagerDao.getSettingValue(co.getKey());
        if (val == null) {
            SettingDefinition<?, ?> def = getDefinitionByKey(co.getKey());
            if (def == null) {
                throw new ConfigurationException(String
                        .format("No SettingDefinition found for key %s", co
                                .getKey()));
            }
            val = (SettingValue<Object>) getSettingFactory()
                    .newSettingValue(def, null);
            if (def.isOptional()) {
                LOG.debug("No value found for optional setting {}", co.getKey());
                this.settingsManagerDao.saveSettingValue(val);
            } else if (def.hasDefaultValue()) {
                LOG
                        .debug("Using default value '{}' for required setting {}", def
                                .getDefaultValue(), co.getKey());
                this.settingsManagerDao.saveSettingValue(val.setValue(def.getDefaultValue()));
            } else if (def.getKey().equals(ServiceSettings.SERVICE_URL)) {
                this.settingsManagerDao.saveSettingValue(val.setValue(URI
                        .create("http://localhost:8080/iceland/service")));
            } else {
                throw new ConfigurationException(String.format(
                        "No value found for required Setting '%s' with no default value.", co
                                .getKey()));
            }
        }
        return val;
    }

    /**
     * Changes a setting. The change is propagated to all Objects that are
     * configured. If the change fails for one of these objects, the setting is
     * reverted to the old value of the setting for all objects.
     *
     * @param newValue
     *              the new value of the setting
     *
     * @throws ConfigurationException
     *                                     if there is a problem changing the setting.
     * @throws ConnectionProviderException
     */
    public void changeSetting(SettingValue<?> newValue)
            throws ConfigurationException, ConnectionProviderException {
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
            throw new IllegalArgumentException(String
                    .format("Invalid type for definition (%s vs. %s)", def
                            .getType(), newValue.getType()));
        }

        SettingValue<?> oldValue = this.settingsManagerDao.getSettingValue(newValue.getKey());
        if (oldValue == null || !oldValue.equals(newValue)) {
            applySetting(def, oldValue, newValue);
            this.settingsManagerDao.saveSettingValue(newValue);
            this.serviceEventBus.submit(new SettingsChangeEvent(def, oldValue, newValue));
        }
    }



    /**
     * @return the {@link SettingValueFactory} to produce values
     */
    public SettingValueFactory getSettingFactory() {
        return this.settingValueFactory;
    }

    /**
     * Deletes all settings and users.
     *
     * @throws ConnectionProviderException
     */
    @Override
    public void deleteAll()
            throws ConnectionProviderException {
        this.settingsManagerDao.deleteAll();
        this.adminUserDao.deleteAll();
    }

    @Override
    public void deleteAdminUser(AdministratorUser user) throws ConnectionProviderException {
        deleteAdminUser(user.getUsername());
    }

    @Override
    public boolean hasAdminUser()
            throws ConnectionProviderException {
        return !getAdminUsers().isEmpty();
    }

    @Override
    public AdministratorUser createAdminUser(String username, String password)
            throws ConnectionProviderException {
        return this.adminUserDao.createAdminUser(username, password);
    }

    @Override
    public void deleteAdminUser(String username)
        throws ConnectionProviderException {
        this.adminUserDao.deleteAdminUser(username);
    }

    @Override
    public AdministratorUser getAdminUser(String username)
        throws ConnectionProviderException {
        return this.adminUserDao.getAdminUser(username);
    }

    @Override
    public Set<AdministratorUser> getAdminUsers()
            throws ConnectionProviderException {
        return this.adminUserDao.getAdminUsers();
    }

    @Override
    public void saveAdminUser(AdministratorUser user)
            throws ConnectionProviderException {
        this.adminUserDao.saveAdminUser(user);
    }

    private class ConfigurableObject {
        private final Method method;
        private final WeakReference<Object> target;
        private final String key;

        /**
         * Constructs a new {@code ConfigurableObject}.
         *
         * @param method
         *               the method of the target
         * @param target
         *               the target object
         * @param key
         *               the settings key
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
         *
         * @param val
         *            the value
         * @throws ConfigurationException
         *                                if an error occurs
         */
        public void configure(SettingValue<?> val)
                throws ConfigurationException {
            configure(val.getValue());
        }

        /**
         * Configures this object with the specified value. Exceptions are
         * wrapped in a {@code ConfigurationException}.
         *
         * @param val
         *            the value
         * @throws ConfigurationException
         *                                if an error occurs
         */
        public void configure(Object val)
                throws ConfigurationException {
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

        private void logAndThrowError(Object val, Throwable t)
                throws ConfigurationException {
            String message = String
                    .format("Error while setting value '%s' (%s) for property '%s' with method '%s'", val,
                            val == null ? null : val.getClass(), getKey(), getMethod());
            LOG.error(message);
            throw new ConfigurationException(message, t);
        }

        @Override
        public String toString() {
            return String
                    .format("ConfigurableObject[key=%s, method=%s, target=%s]", getKey(), getMethod(),
                            getTarget().get());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getMethod(), getTarget(), getKey());
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
            if (getMethod() != other.getMethod() && (getMethod() == null ||
                                                     !getMethod().equals(other
                                                             .getMethod()))) {
                return false;
            }
            if (getTarget() != other.getTarget() && (getTarget() == null ||
                                                     !getTarget().equals(other
                                                             .getTarget()))) {
                return false;
            }
            return !((getKey() == null) ? (other.getKey() != null) : !getKey()
                    .equals(other.getKey()));
        }
    }

}
