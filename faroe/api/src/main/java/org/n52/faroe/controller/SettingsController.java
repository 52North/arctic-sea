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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import javax.inject.Inject;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingValue;
import org.n52.faroe.dao.ServicesDao;
import org.n52.faroe.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is responsible for creation of endpoints to modify settings more easily.
 */

@RestController
@RequestMapping({"/services/{service}/settings"})
public class SettingsController {

  private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);
  private static final Gson gson = new Gson();
  @Inject
  private ServicesDao servicesDao;

  @GetMapping
  public ResponseEntity<Object> getSettings(@PathVariable("service") String serviceName) {
    LOG.info("Getting Settings");
    try {
      Map<SettingDefinition<?>, SettingValue<?>> settings = servicesDao.getServiceByName(serviceName)
          .getSettingsService().getSettings();
      return new ResponseEntity<>(settings, HttpStatus.OK);
    } catch (Exception e) {
      LOG.error(String.format("Couldn't fetch settings for service %s", serviceName));
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PostMapping
  public ResponseEntity<Object> updateSettings(@PathVariable("service") String serviceName,
      @RequestBody String newSettings) {
    LOG.info("Updating Settings");
    try {
      final JsonObject settings = new JsonParser().parse(newSettings).getAsJsonObject();
      final Service service = servicesDao.getServiceByName(serviceName).getSettingsService().getSetting()
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      LOG.error("Update Request Failed");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  public ResponseEntity<Object> deleteSettings(@PathVariable("service") String serviceName) {
    LOG.info("Deleting Settings");
    try {
      servicesDao.getServiceByName(serviceName).getSettingsService().deleteAll();
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      LOG.error("Couldn't delete all settings!");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}