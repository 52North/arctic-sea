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
import org.n52.faroe.SettingValue;
import org.n52.faroe.json.JsonSettingValue;
import org.n52.faroeREST.springrest.settings.SettingsAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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

    @GetMapping(path = "/definitions")
    public Collection<SettingDefinition<?>> getSettingDefinitions() {
        return this.api.getSettingDefinitions();

    }

    @PostMapping(path = "/definitions", consumes = "application/json")
    public void addSettingDefinitions(@RequestBody Collection<SettingDefinition<?>> value) {
        this.api.addSettingDefinitions(value);
    }

    @CrossOrigin(origins = "http://localhost:3000/groups")
    @GetMapping(path = "/definitions/groups")
    public Set<String> getGroups() {
        return this.api.getGroups();
    }

    @GetMapping(path = "/definitions/groups/{groupTitle}")
    public Collection<SettingDefinition<?>> getSettingDefinitionByTitle(@PathVariable String groupTitle) {
        return this.api.getSettingsByTitle(groupTitle);
    }

    @PutMapping(path = "/settings", consumes = "application/json")
    public void updateSettingValue(@RequestBody JsonSettingValue<?> value) {
        this.api.updateSettingValue(value);
    }

    @GetMapping("/settings")
    public Collection<SettingValue<?>> getSettingValues() {
        return this.api.getSettingValues();
    }

    @GetMapping("/settings/{groupTitle}")
    public Collection<SettingValue<?>> getSettingValuesByGroup(String groupTitle) {
        return this.api.getSettingValuesByGroup(groupTitle);
    }

    @DeleteMapping("/settings/{setting}")
    public void deleteSettingValue(@PathVariable String setting) {
        this.api.deleteSettingValue(setting);
    }
}
