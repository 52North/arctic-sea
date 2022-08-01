/*
 * Copyright (C) 2015-2022 52°North Spatial Information Research GmbH
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
package org.n52.faroeREST.springrest.settings;

import java.util.*;

import org.n52.faroe.SettingDefinition;
import org.n52.faroeREST.springrest.entities.Groups;

public interface SettingsAPI {

	public Collection<SettingAPIDao> getSettings();
	
	public Set<String> getGroups();

	public Collection<SettingDefinition<?>> getSettingsbyTitle(String groupTitle);

	public String addSettings(SettingAPIDao group);

	public Collection<SettingDefinition<?>> updateSettings(Collection<SettingDefinition<?>> group);

	public Set<SettingDefinition<?>> deleteGroup();

	public String deleteSettings(Collection<SettingDefinition<?>> setting);

	public String deleteAllSettings();
}
