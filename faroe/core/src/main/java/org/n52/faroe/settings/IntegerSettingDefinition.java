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
package org.n52.faroe.settings;

import org.n52.faroe.AbstractSettingDefinition;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingType;

/**
 * {@link SettingDefinition} for {@code Integer}s.
 *
 * @since 1.0.0
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 */
public class IntegerSettingDefinition extends AbstractSettingDefinition<Integer> {

    private static final long serialVersionUID = 5224951859986317447L;
    private Integer minimum;
    private Integer maximum;
    private boolean exclusiveMaximum;
    private boolean exclusiveMinimum;

    /**
     * Constructs a new {@code IntegerSettingDefinition}.
     */
    public IntegerSettingDefinition() {
        super(SettingType.INTEGER);
    }

    /**
     * Get the value of minimum.
     *
     * @return the value of minimum
     */
    public Integer getMinimum() {
        return minimum;
    }

    /**
     * @return whether a minimum value is set
     */
    public boolean hasMinimum() {
        return getMinimum() != null;
    }

    /**
     * Set the value of minimum.
     *
     * @param minimum new value of minimum
     *
     * @return this
     */
    public IntegerSettingDefinition setMinimum(Integer minimum) {
        this.minimum = minimum;
        return this;
    }

    /**
     * Get the value of maximum.
     *
     * @return the value of maximum
     */
    public Integer getMaximum() {
        return maximum;
    }

    /**
     * @return whether a maximum value is set
     */
    public boolean hasMaximum() {
        return getMaximum() != null;
    }

    /**
     * Set the value of maximum.
     *
     * @param maximum new value of maximum
     *
     * @return this
     */
    public IntegerSettingDefinition setMaximum(Integer maximum) {
        this.maximum = maximum;
        return this;
    }

    /**
     * Get the value of exclusiveMaximum.
     *
     * @return the value of exclusiveMaximum
     */
    public boolean isExclusiveMaximum() {
        return exclusiveMaximum;
    }

    /**
     * Set the value of exclusiveMaximum.
     *
     * @param exclusiveMaximum new value of exclusiveMaximum
     *
     * @return this
     */
    public IntegerSettingDefinition setExclusiveMaximum(boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
        return this;
    }

    /**
     * Get the value of exclusiveMinimum.
     *
     * @return the value of exclusiveMinimum
     */
    public boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }

    /**
     * Set the value of exclusiveMinimum.
     *
     * @param exclusiveMinimum new value of exclusiveMinimum
     *
     * @return this
     */
    public IntegerSettingDefinition setExclusiveMinimum(boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
        return this;
    }
}
