/*
 * Copyright 2015-2020 52°North Initiative for Geospatial Open Source
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

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

import org.n52.faroe.SettingDefinitionGroup;

public class AbstractSettingsDefinitionTest {

    @Test
    public void isNotEquals() {
        assertThat(getBooleanSettingOne().equals(getBooleanSettingTwo()), is(false));
    }

    private BooleanSettingDefinition getBooleanSettingOne() {
        BooleanSettingDefinition def = getDefaultBooleanSetting();
        def.setKey("key.one");
        def.setTitle("Test setting one");
        def.setDescription("Test setting one");
        return def;
    }

    private BooleanSettingDefinition getBooleanSettingTwo() {
        BooleanSettingDefinition def = getDefaultBooleanSetting();
        def.setKey("key.two");
        def.setTitle("Test setting two");
        def.setDescription("Test setting two");
        return def;
    }

    private BooleanSettingDefinition getDefaultBooleanSetting() {
        BooleanSettingDefinition def = new BooleanSettingDefinition();
        def.setGroup(getGroup());
        def.setOrder(1);
        def.setDefaultValue(false);
        def.setOptional(true);
        return def;
    }

    private SettingDefinitionGroup getGroup() {
        SettingDefinitionGroup group = new SettingDefinitionGroup();
        group.setTitle("Test");
        group.setOrder(2);
        return group;
    }

}
