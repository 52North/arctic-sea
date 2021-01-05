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

import org.n52.faroe.AbstractSettingDefinition;
import org.n52.faroe.SettingType;
import org.n52.janmayen.i18n.MultilingualString;

public class MultilingualStringSettingDefinition extends AbstractSettingDefinition<MultilingualString> {

    private static final long serialVersionUID = -3680807551449370597L;

    public MultilingualStringSettingDefinition() {
        super(SettingType.MULTILINGUAL_STRING);
    }
}
