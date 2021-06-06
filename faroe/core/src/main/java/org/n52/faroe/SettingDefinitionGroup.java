/*
 * Copyright 2015-2021 52°North Initiative for Geospatial Open Source
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

import java.util.Objects;

import com.google.common.base.MoreObjects;

/**
 * Class to group ISettingDefinitions. Not needed by the service but only for representation in the GUI.
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 * @since 1.0.0
 */
public class SettingDefinitionGroup extends AbstractOrdered {

    private String title;
    private String description;
    private boolean showInDefaultSetting = true;

    public SettingDefinitionGroup(String title, String description, float order) {
        super(order);
        this.title = title;
        this.description = description;
    }

    public SettingDefinitionGroup(String title, String description) {
        this(title, description, 0.0f);
    }

    public SettingDefinitionGroup(String title, float order) {
        this(title, null, order);
    }

    public SettingDefinitionGroup(String title) {
        this(title, null, 0.0f);
    }

    public SettingDefinitionGroup() {
        this(null, null, 0.0f);
    }

    /**
     * @return the title of this group
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this group.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return if this group has a non empty title
     */
    public boolean hasTitle() {
        return hasStringProperty(getTitle());
    }

    /**
     * @return the description for this group
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for this group.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return if this group has a non empty description
     */
    public boolean hasDescription() {
        return hasStringProperty(getDescription());
    }

    /**
     * Set if this settings group should be displayed in default settings
     *
     * @param show Display in default settings
     */
    public void setShowInDefaultSettings(boolean show) {
        this.showInDefaultSetting = show;
    }

    /**
     * Should this group be displayed in default settings
     *
     * @return {@code true}, if this group should be displayed in default settings
     */
    public boolean isShowInDefaultSettings() {
        return showInDefaultSetting;
    }

    /**
     * Checks if the parameter is not null and not empty.
     *
     * @param s the string to test
     *
     * @return if it is not null and not empty
     */
    protected boolean hasStringProperty(String s) {
        return s != null && !s.isEmpty();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SettingDefinitionGroup other = (SettingDefinitionGroup) obj;
        return Objects.equals(this.getTitle(), other.getTitle());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", getTitle())
                .add("description", getDescription())
                .add("showInDefaultSetting", isShowInDefaultSettings())
                .toString();
    }

    @Override
    protected String getSuborder() {
        return getTitle();
    }

}
