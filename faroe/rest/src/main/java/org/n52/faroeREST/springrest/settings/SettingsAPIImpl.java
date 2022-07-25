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
import java.util.stream.Collectors;


import javax.inject.Inject;

import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingsService;
import org.n52.faroeREST.springrest.entities.Groups;
import org.springframework.stereotype.Service;


@Service
public class SettingsAPIImpl implements SettingsAPI {

	@Inject
	private SettingsService service;


	@Override
	public Set<SettingDefinition<?>> getSettings() {
		return service.getSettingDefinitions();
	}

	List<Groups> list;

	public SettingsAPIImpl() {
		list=new ArrayList<>();
		list.add(new Groups("Desc","This is desc"));
		list.add(new Groups("Desc2","This is desc2"));
	}


	@Override
	public Groups getSettingsbyTitle(String groupTitle) {

		Groups g=null;
		for(Groups group : list) {
			if(group.getTitle() == groupTitle) {
				g=group;
				break;
			}
		}
		return g;

	}

	@Override
	public Groups addSettings(Groups group) {

		list.add(group);

		return group;
	}

	@Override
	public Groups updateSettings(Groups group) {

		list.forEach(e -> {
			if(e.getTitle() == group.getTitle()) {
				e.setDescription(group.getDescription());
			}

	});

		return group;
	}

	@Override
	public Groups deleteGroup(String groupTitle) {
		list=this.list.stream().filter(e->e.getTitle()!=groupTitle).collect(Collectors.toList());
		return null;
	}

}
