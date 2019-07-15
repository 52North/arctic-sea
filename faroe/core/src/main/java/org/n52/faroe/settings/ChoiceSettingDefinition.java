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


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.n52.faroe.AbstractSettingDefinition;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingType;
import org.n52.janmayen.stream.MoreCollectors;

/**
 * {@link SettingDefinition} resulting in a drop down menu offering different
 * options.By default the options are sorted by their display name (it's set
 * to value if not provided).
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @author <a href="mailto:e.h.juerrens@52north.org">Eike Hinderk J&uuml;rrens</a>
 *
 * @since 1.0.0
 *
 */
public class ChoiceSettingDefinition extends AbstractSettingDefinition<String> {

    private static final long serialVersionUID = 4783164088023177712L;
    private final Map<String, String> options = new HashMap<>();

    public ChoiceSettingDefinition() {
        super(SettingType.CHOICE);
    }

    public Map<String, String> getOptions() {
        return Collections.unmodifiableMap(options.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(MoreCollectors.toLinkedHashMap()));
    }

    public boolean hasOption(String value) {
        return this.options.containsKey(value);
    }

    public ChoiceSettingDefinition setOptions(Map<String, String> options) {
        this.options.clear();
        for (Entry<String, String> entry : options.entrySet()) {
            addOption(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public ChoiceSettingDefinition addOption(String option) {
        String value = Objects.requireNonNull(option);
        this.options.put(value, value);
        return this;
    }

    public ChoiceSettingDefinition addOption(String option, String displayName) {
        this.options.put(
                Objects.requireNonNull(option),
                Objects.requireNonNull(displayName));
        return this;
    }

}
