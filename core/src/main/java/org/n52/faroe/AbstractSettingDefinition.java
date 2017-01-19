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

/**
 * Generic implementation of <code>SettingDefinition</code>.
 *
 * @param <T> the type of the value
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public abstract class AbstractSettingDefinition<T> extends AbstractOrdered implements SettingDefinition<T> {

    private boolean optional;
    private String identifier;
    private String title;
    private String description;
    private SettingDefinitionGroup group;
    private SettingType type;
    private T defaultValue;

    /**
     * @param type the <code>SettingType</code> of this setting definition
     */
    protected AbstractSettingDefinition(SettingType type) {
        this.type = type;
    }

    @Override
    public String getKey() {
        return identifier;
    }

    @Override
    public void setKey(String k) {
        this.identifier = k;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String d) {
        this.description = d;
    }

    @Override
    public boolean hasDescription() {
        return hasStringProperty(getDescription());
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String t) {
        this.title = t;
    }

    @Override
    public boolean hasTitle() {
        return hasStringProperty(getTitle());
    }

    @Override
    public boolean isOptional() {
        return optional;
    }

    @Override
    public void setOptional(boolean o) {
        this.optional = o;
    }

    @Override
    public SettingDefinitionGroup getGroup() {
        return group;
    }

    @Override
    public void setGroup(SettingDefinitionGroup g) {
        this.group = g;
    }

    @Override
    public boolean hasGroup() {
        return getGroup() != null;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void setDefaultValue(T d) {
        this.defaultValue = d;
    }

    @Override
    public boolean hasDefaultValue() {
        return getDefaultValue() != null;
    }

    protected boolean hasStringProperty(String s) {
        return s != null && !s.isEmpty();
    }

    @Override
    public int hashCode() {
        return (getKey() != null) ? getKey().hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbstractSettingDefinition) {
            AbstractSettingDefinition<?> o = (AbstractSettingDefinition<?>) obj;
            return (getKey() == null ? o.getKey() == null : getKey().equals(o.getKey())) &&
                   (getTitle() == null ? o.getTitle() == null : getTitle().equals(o.getTitle())) &&
                   (getDescription() == null ? o.getDescription() == null : getDescription()
                           .equals(o.getDescription())) &&
                   (getGroup() == null ? o.getGroup() == null : getGroup().equals(o.getGroup())) &&
                   (getDefaultValue() == null ? o.getDefaultValue() == null : getDefaultValue().equals(o
                           .getDefaultValue())) &&
                   (getType() == o.getType()) &&
                   (isOptional() == o.isOptional());
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s[key=%s]", getClass().getSimpleName(), getKey());
    }

    @Override
    public SettingType getType() {
        return this.type;
    }

    void setType(SettingType type) {
        this.type = type;
    }

    @Override
    protected String getSuborder() {
        return getTitle();
    }
}
