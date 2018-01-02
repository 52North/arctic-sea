/*
 * Copyright 2017-2018 52°North Initiative for Geospatial Open Source
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

import java.io.File;

import org.n52.faroe.AbstractSettingDefinition;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingType;

/**
 * {@link SettingDefinition} for {@code File}s.
 *
 * @since 1.0.0
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 */
public class FileSettingDefinition extends AbstractSettingDefinition<File> {

    /**
     * Constructs a new {@code FileSettingDefinition}.
     */
    public FileSettingDefinition() {
        super(SettingType.FILE);
    }
}
