/*
 * Copyright 2017 52°North Initiative for Geospatial Open Source
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
package org.n52.faroe;

import static java.util.stream.Collectors.toSet;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.n52.faroe.annotation.Configurable;
import org.n52.faroe.annotation.Setting;
import org.n52.janmayen.event.EventBus;
import org.n52.janmayen.function.Functions;

/**
 * Class to handle the settings and configuration of the service. Allows other classes to change, delete, and declare
 * settings and to create, modify and delete administrator users. Classes can subscribe to specific settings using the
 * {@code Configurable} and {@code Setting} annotations. To be recognized by the SettingsManager
 * {@link #configure(java.lang.Object)} has to be called for every object that wants to receive settings. All other
 * classes have to call {@code configure(java.lang.Object)} manually.
 *
 *
 * @see SettingDefinition
 * @see SettingValue
 * @see Configurable
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public class SettingsServiceImpl implements SettingsService {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsServiceImpl.class);
    private final Map<String, Set<ConfigurableObject>> configurableObjects = new HashMap<>();
    private final ReadWriteLock configurableObjectsLock = new ReentrantReadWriteLock();
    private Set<SettingDefinition<?>> definitions;
    private Map<String, SettingDefinition<?>> definitionByKey;
    private SettingsDao settingsManagerDao;
    private SettingValueFactory settingValueFactory;
    private EventBus serviceEventBus;

    @Inject
    public void setServiceEventBus(EventBus serviceEventBus) {
        this.serviceEventBus = serviceEventBus;
    }

    @Inject
    public void setSettingValueFactory(SettingValueFactory settingValueFactory) {
        this.settingValueFactory = settingValueFactory;
    }

    @Inject
    public void setSettingsManagerDao(SettingsDao settingsManagerDao) {
        this.settingsManagerDao = settingsManagerDao;
    }

    @Inject
    public void setSettingDefinitions(Collection<SettingDefinition<?>> def) {
        this.definitions = new HashSet<>(def.size());
        this.definitionByKey = new HashMap<>(def.size());
        def.forEach(definition -> {
            this.definitions.add(definition);
            if (this.definitionByKey.put(definition.getKey(), definition) != null) {
                LOG.warn("Duplicate setting definition for key {}", definition.getKey());
            }
        });
    }

    /**
     * Gets all {@code SettingDefinition}s known by this class.
     *
     * @return the definitions
     */
    @Override
    public Set<SettingDefinition<?>> getSettingDefinitions() {
        return Collections.unmodifiableSet(this.definitions);
    }


    @Override
    public void configureOnce(Object object) throws ConfigurationError {
        configure(object, false);
    }

    /**
     * Configure {@code o} with the required settings. All changes to a setting required by the object will be applied.
     *
     * @param object the object to configure
     *
     * @throws ConfigurationError if there is a problem configuring the object
     * @see Configurable
     * @see Setting
     */
    @Override
    public void configure(Object object) throws ConfigurationError {
        configure(object, true);
    }


    private void configure(Object object, boolean persist) throws ConfigurationError {
        Class<?> clazz = object.getClass();
        if (clazz.getAnnotation(Configurable.class) == null) {
            return;
        }
        LOG.debug("Configuring object {}", object);
        for (Method method : clazz.getMethods()) {
            Setting s = method.getAnnotation(Setting.class);
            if (s != null) {
                String key = s.value();
                if (key == null || key.isEmpty()) {
                    throw new ConfigurationError(String.format("Invalid value for @Setting: '%s'", key));
                } else if (getDefinitionByKey(key) == null && s.required()) {
                    throw noSettingDefinitionFound(key);
                } else if (method.getParameterTypes().length != 1) {
                    throw new ConfigurationError(String.format(
                            "Method %s annotated with @Setting in %s has a invalid method signature", method, clazz));
                } else if (!Modifier.isPublic(method.getModifiers())) {
                    throw new ConfigurationError(String.format(
                            "Non-public method %s annotated with @Setting in %s", method, clazz));
                } else {
                    configure(new ConfigurableObject(method, object, key, s.required()), persist);
                }
            }
        }
    }

    private void configure(ConfigurableObject co, boolean persist) {
        LOG.debug("Configuring {}", co);
        if (persist) {
            this.configurableObjectsLock.writeLock().lock();
            try {
                this.configurableObjects.computeIfAbsent(co.getKey(), Functions.forSupplier(HashSet::new)).add(co);
            } finally {
                this.configurableObjectsLock.writeLock().unlock();
            }
        }
        try {
            co.configure(getSettingValue(co));
        } catch (RuntimeException cpe) {
            throw new ConfigurationError("Exception configuring " + co.getKey(), cpe);
        }
    }

    /**
     * Get the definition that is defined with the specified key.
     *
     * @param key the key
     *
     * @return the definition or {@code null} if there is no definition for the key
     */
    @Override
    public SettingDefinition<?> getDefinitionByKey(String key) {
        return this.definitionByKey.get(key);
    }

    /**
     * Gets the value of the setting defined by {@code key}.
     *
     * @param <T> the type of the setting and value
     * @param key the definition of the setting
     *
     * @return the value of the setting
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> SettingValue<T> getSetting(SettingDefinition<T> key) {
        return (SettingValue<T>) this.settingsManagerDao.getSettingValue(key.getKey());
    }

    /**
     * Gets the value of the setting defined by {@code key}.
     *
     * @param <T> the type of the setting and value
     * @param key the id of the setting
     *
     * @return the value of the setting
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> SettingValue<T> getSetting(String key) {
        SettingDefinition<?> def = getDefinitionByKey(key);
        if (def == null) {
            return null;
        }
        return (SettingValue<T>) getSetting(def);
    }

    /**
     * Gets all values for all definitions. If there is no value for a definition {@code null} is added to the map.
     *
     * @return all values by definition
     */
    @Override
    public Map<SettingDefinition<?>, SettingValue<?>> getSettings() {
        Set<SettingValue<?>> values = this.settingsManagerDao.getSettingValues();
        Map<SettingDefinition<?>, SettingValue<?>> settingsByDefinition = new HashMap<>(values.size());
        values.forEach(value -> {
            final SettingDefinition<?> definition = getDefinitionByKey(value.getKey());
            if (definition == null) {
                LOG.warn("No definition for '{}' found.", value.getKey());
            } else {
                settingsByDefinition.put(definition, value);
            }
        });
        HashSet<SettingDefinition<?>> nullValues = new HashSet<>(getSettingDefinitions());
        nullValues.removeAll(settingsByDefinition.keySet());
        nullValues.forEach(s -> settingsByDefinition.put(s, null));
        return settingsByDefinition;
    }

    /**
     * Deletes the setting defined by {@code setting}.
     *
     * @param setting the definition
     *
     * @throws ConfigurationError if there is a problem deleting the setting
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void deleteSetting(SettingDefinition<?> setting)
            throws ConfigurationError {
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
    @Override
    public Set<String> getKeys() {
        return getSettingDefinitions().stream().map(SettingDefinition::getKey).collect(toSet());
    }

    /**
     * Applies the a new setting to all {@code ConfiguredObject}s. If an error occurs the the old value is reapplied.
     *
     * @param setting  the definition
     * @param oldValue the old value (or {@code null} if there is none)
     * @param newValue the new value (or {@code null} if there is none)
     *
     * @throws ConfigurationError if there is a error configuring the objects
     */
    private void applySetting(SettingDefinition<?> setting, SettingValue<?> oldValue, SettingValue<?> newValue)
            throws ConfigurationError {
        List<ConfigurableObject> changed = new LinkedList<>();
        ConfigurationError e = null;
        configurableObjectsLock.readLock().lock();
        try {
            Set<ConfigurableObject> cos = configurableObjects.get(setting.getKey());
            if (cos != null) {
                for (ConfigurableObject co : cos) {
                    try {
                        co.configure(newValue.getValue());
                    } catch (ConfigurationError ce) {
                        e = ce;
                        break;
                    } finally {
                        changed.add(co);
                    }
                }
                if (e != null) {
                    LOG.debug("Reverting setting...");
                    changed.stream().forEach(co -> {
                        try {
                            co.configure(oldValue.getValue());
                        } catch (ConfigurationError ce) {
                            /* there is nothing we can do... */
                            LOG.error("Error reverting setting!", ce);
                        }
                    });
                    throw e;
                }
            }
        } finally {
            configurableObjectsLock.readLock().unlock();
        }
    }

    private SettingValue<Object> getSettingValue(ConfigurableObject co) {
        return getSettingValue(co.getKey(), co.isRequired());
    }

    @SuppressWarnings("unchecked")
    private SettingValue<Object> getSettingValue(String key, boolean required) {
        SettingValue<Object> val = (SettingValue<Object>) this.settingsManagerDao.getSettingValue(key);
        if (val != null) {
            return val;
        }
        SettingDefinition<?> def = getDefinitionByKey(key);
        if (def == null) {
            if (required) {
                throw noSettingDefinitionFound(key);
            } else {
                return new NullSettingValue<>(key);
            }
        } else {
            val = (SettingValue<Object>) getSettingFactory().newSettingValue(def, null);
            if (def.isOptional()) {
                LOG.debug("No value found for optional setting {}", key);
                this.settingsManagerDao.saveSettingValue(val);
            } else if (def.hasDefaultValue()) {
                LOG.debug("Using default value '{}' for required setting {}", def.getDefaultValue(), key);
                val.setValue(def.getDefaultValue());
                this.settingsManagerDao.saveSettingValue(val);
            } else {
                throw new ConfigurationError(String.format(
                        "No value found for required Setting '%s' with no default value.", key));
            }
            return val;
        }
    }

    /**
     * Changes a setting. The change is propagated to all Objects that are configured. If the change fails for one of
     * these objects, the setting is reverted to the old value of the setting for all objects.
     *
     * @param newValue the new value of the setting
     *
     * @throws ConfigurationError if there is a problem changing the setting.
     */
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void changeSetting(SettingValue<?> newValue)
            throws ConfigurationError {
        if (newValue == null) {
            throw new NullPointerException("newValue can not be null");
        }
        if (newValue.getKey() == null) {
            throw new NullPointerException("newValue.key can not be null");
        }
        SettingDefinition<?> def = getDefinitionByKey(newValue.getKey());

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
    @Override
    public SettingValueFactory getSettingFactory() {
        return this.settingValueFactory;
    }

    /**
     * Deletes all settings and users.
     */
    @Override
    public void deleteAll() {
        this.settingsManagerDao.deleteAll();
    }

    @Override
    public void reconfigure() {
        LOG.trace("Reconfiguring all objects");
        this.configurableObjectsLock.readLock().lock();
        try {
            configurableObjects.forEach((key, objects) -> {
                boolean required = objects.stream().anyMatch(ConfigurableObject::isRequired);
                SettingValue<Object> v = SettingsServiceImpl.this.getSettingValue(key, required);
                objects.forEach(co -> co.configure(v));
            });
        } finally {
            this.configurableObjectsLock.readLock().unlock();
        }
    }

    private static ConfigurationError noSettingDefinitionFound(String key) {
        return new ConfigurationError(String.format("No SettingDefinition found for key %s", key));
    }

    private static final class NullSettingValue<T> implements SettingValue<T> {

        private final String key;

        NullSettingValue(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public void setKey(String key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T getValue() {
            return null;
        }

        @Override
        public void setValue(T value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public SettingType getType() {
            throw new UnsupportedOperationException();
        }

    }

    private static class ConfigurableObject {

        private final Method method;
        private final WeakReference<Object> target;
        private final String key;
        private final boolean required;

        /**
         * Constructs a new {@code ConfigurableObject}.
         *
         * @param method   the method of the target
         * @param target   the target object
         * @param key      the settings key
         * @param required if the setting is required
         */
        ConfigurableObject(Method method, Object target, String key, boolean required) {
            this.method = method;
            this.target = new WeakReference<>(target);
            this.key = key;
            this.required = required;
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
         * @param val the value
         *
         * @throws ConfigurationError if an error occurs
         */
        public void configure(SettingValue<?> val)
                throws ConfigurationError {
            configure(val.getValue());
        }

        /**
         * Configures this object with the specified value. Exceptions are wrapped in a {@code ConfigurationError}.
         *
         * @param val the value
         *
         * @throws ConfigurationError if an error occurs
         */
        public void configure(Object val)
                throws ConfigurationError {
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
                throws ConfigurationError {
            String message = String
                    .format("Error while setting value '%s' (%s) for property '%s' with method '%s'", val,
                            val == null ? null : val.getClass(), getKey(), getMethod());
            LOG.error(message);
            throw new ConfigurationError(message, t);
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
            if (getMethod() != other.getMethod() && (getMethod() == null || !getMethod().equals(other.getMethod()))) {
                return false;
            }
            if (getTarget() != other.getTarget() && (getTarget() == null || !getTarget().equals(other.getTarget()))) {
                return false;
            }
            return !((getKey() == null) ? (other.getKey() != null) : !getKey().equals(other.getKey()));
        }

        /**
         * @return if this setting is required
         */
        public boolean isRequired() {
            return required;
        }
    }

}
