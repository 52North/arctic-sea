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
package org.n52.faroeREST.springrest.controller;

import java.util.Collection;
import java.util.Set;

import org.n52.faroe.SettingDefinition;
import org.n52.faroeREST.springrest.entities.Groups;
import org.n52.faroeREST.springrest.settings.SettingsAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {

	@Autowired
	private SettingsAPI api;

	@GetMapping(path = "/settings")
	public Set<SettingDefinition<?>> getSettings(){

		return this.api.getSettings();

	}
	
	@GetMapping(path = "/settings/groups")
	public Set<String> getGroups(){

		return this.api.getGroups();

	}

	@GetMapping(path = "/settings/{groupTitle}")
	public Collection<SettingDefinition<?>> getSettingsbyTitle(@PathVariable String groupTitle) {
		return this.api.getSettingsbyTitle(groupTitle);
	}


	@PostMapping(path = "/settings", consumes = "application/json")
	public Set<SettingDefinition<?>> addGroup(@RequestBody Groups group) {

		return this.api.addSettings(group);

	}

	@PutMapping(path = "/settings", consumes = "application/json")
	public Groups updateGroup(@RequestBody Groups group) {

		return this.api.updateSettings(group);

	}

	@DeleteMapping(path = "/settings")
	public Set<SettingDefinition<?>> deleteGroup() {
			return this.api.deleteGroup();
	}		
}
