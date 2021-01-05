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
package org.n52.faroe.settings;

import org.joda.time.DateTime;

import org.n52.faroe.AbstractSettingDefinition;
import org.n52.faroe.SettingType;
import org.n52.janmayen.Times;

/**
 * @author <a href="mailto:c.hollmann@52north.org">Carsten Hollmann</a>
 * @since 1.0.0
 *
 */
public class DateTimeSettingDefinition extends AbstractSettingDefinition<DateTime> {

    private static final long serialVersionUID = 7547015368818138572L;

    /**
     * constructor
     */
    public DateTimeSettingDefinition() {
        super(SettingType.TIMEINSTANT);
    }

    /**
     * Setter for default value as {@link String}. Parses the string to {@link DateTime} object
     *
     * @param value Default value as String
     *
     * @return this
     */
    public DateTimeSettingDefinition setDefaultStringValue(String value) {
        setDefaultValue(Times.decodeDateTime(value));
        return this;
    }

}
