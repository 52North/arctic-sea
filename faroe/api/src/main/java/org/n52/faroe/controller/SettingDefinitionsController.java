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
package org.n52.faroe.controller;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.dao.ServicesDao;
import org.n52.faroe.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This endpoint is responsible for fetching all the setting definitions for a a particular service
 */

@RestController
@RequestMapping("/services/{service}/definitions")
public class SettingDefinitionsController {

  private static final Logger LOG = LoggerFactory.getLogger(SettingDefinitionsController.class);
  private static final Gson gson = new Gson();
  @Inject
  private ServicesDao servicesDao;

  @GetMapping
  public ResponseEntity<Object> getSettingDefinitons(@PathVariable("service") String serviceName) {
    LOG.info("Getting definitions");
    try {
      final Service service = servicesDao.getServiceByName(serviceName);
      final Set<SettingDefinition<?>> settingDefinitionSet = service.getSettingsService()
          .getSettingDefinitions();
      final Map<String, Object> response = new HashMap<>();
      response.put("Name", service.getName());
      response.put("Setting Definitions", gson.toJson(settingDefinitionSet));
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      LOG.error(
          String.format("Couldn't fetch setting definitions for the service: %s", serviceName));
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Object> getSettingDefinitionById(
      @PathVariable("service") String serviceName, @PathVariable("id") String definitionId) {
    LOG.info(String.format("Getting definition for service %s", serviceName));
    try {
      final Service service = servicesDao.getServiceByName(serviceName);
      final SettingDefinition<?> requiredDefinition = service.getSettingsService()
          .getSettingDefinitions()
          .stream().filter(settingDefinition -> settingDefinition.getKey().equals(definitionId))
          .findFirst().orElseGet(null);
      final Map<String, Object> response = new HashMap<>();
      response.put("Name", service.getName());
      response.put("Setting Definition", gson.toJson(requiredDefinition));
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      LOG.error(String
          .format("Couldn't fetch setting definition with id %s for the service name %s",
              definitionId, serviceName));
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(value = "/{id}")
  public void updateSettingDefinition(@PathVariable("service") String serviceName,
      @PathVariable("id") String definitionId) {

  }
}
