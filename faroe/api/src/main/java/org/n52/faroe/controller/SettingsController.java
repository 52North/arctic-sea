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

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.n52.faroe.SettingDefinition;
import org.n52.faroe.SettingValue;
import org.n52.faroe.SettingValueFactory;
import org.n52.faroe.SettingsService;
import org.n52.faroe.dao.ServicesDao;
import org.n52.faroe.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

import static org.n52.faroe.Constants.*;

/**
 * This controller is responsible for creation of endpoints to modify settings more easily.
 */
//TODO Ask when to use reconfigure method
@RestController
@RequestMapping({"/services/{service}/settings"})
public class SettingsController {

	private static final Logger LOG = LoggerFactory.getLogger(SettingsController.class);

	@Inject
	private ServicesDao servicesDao;

	@GetMapping
	public ResponseEntity<Object> getSettings(@PathVariable("service") String serviceName) {
		LOG.info("Getting Settings");
		try {
			final Map<SettingDefinition<?>, SettingValue<?>> settings = servicesDao.getServiceByName(serviceName)
					.getSettingsService().getSettings();
			return new ResponseEntity<>(settings, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(String.format("Couldn't fetch settings for service %s", serviceName));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping
	public ResponseEntity<Object> addNewSettings(@PathVariable("service") String serviceName,
												 @RequestBody String requestedSettings) {
		LOG.info("Adding new settings");
		try {
			final JsonObject newSettings = new JsonParser().parse(requestedSettings).getAsJsonObject();
			final Service service = servicesDao.getServiceByName(serviceName);
			final SettingValueFactory settingValueFactory = service.getSettingsService().getSettingFactory();
			newSettingGenerator(newSettings, settingValueFactory);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("Couldn't add new Settings");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping({"/{settingId}"})
	public ResponseEntity<Object> deleteSettings(@PathVariable("service") String serviceName, @PathVariable("settingId") String settingId) {
		LOG.info(String.format("Deleting setting id %s for service %s", settingId, serviceName));
		try {
			final SettingsService settingsService = servicesDao.getServiceByName(serviceName).getSettingsService();
			settingsService.deleteSetting(settingsService.getDefinitionByKey(settingId));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(String.format("Couldn't delete settings with the settings id %s", settingId));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping({"/{definitionId}"})
	public ResponseEntity<Object> updateSettings(@PathVariable("service") String serviceName, @PathVariable("definitionId") String settingId, @RequestBody String newSettings) {
		LOG.info(String.format("Updating Setting with id %s for service %s", settingId, serviceName));
		try {
			final JsonObject newSettingsJson = new JsonParser().parse(newSettings).getAsJsonObject();
			final SettingsService settingsService = servicesDao.getServiceByName(serviceName).getSettingsService();
			final SettingValue updatedValue = newSettingGenerator(newSettingsJson, settingsService.getSettingFactory());
			settingsService.changeSetting(updatedValue);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			LOG.error(String.format("Couldn't update settings with setting id %s for service %s ", settingId, serviceName));
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * For PUT operations, the unchanged values will be sent as part of form input.
	 * For POST operations, all the values will need part of the input payload.
	 */
	private SettingValue<?> newSettingGenerator(JsonObject newSettings, SettingValueFactory settingValueFactory) {
		return Optional.ofNullable(newSettings.get(TYPE)).map(type -> {
			Preconditions.checkNotNull(newSettings.get(KEY));
			Preconditions.checkNotNull(newSettings.get(VALUE));
			switch (type.getAsString().toLowerCase()) {
				case "boolean":
					return settingValueFactory.newBooleanSettingValue(newSettings.get(KEY).getAsString(), newSettings.get(VALUE).getAsString());
				case "file":
					return settingValueFactory.newFileSettingValue(newSettings.get(KEY).getAsString(), newSettings.get(VALUE).getAsString());
				case "uri":
					return settingValueFactory.newUriSettingValue(newSettings.get(KEY).getAsString(), newSettings.get(VALUE).getAsString());
				case "numeric":
					return settingValueFactory.newNumericSettingValue(newSettings.get(KEY).getAsString(), newSettings.get(VALUE).getAsString());
				case "datetime":
					return settingValueFactory.newDateTimeSettingValue(newSettings.get(KEY).getAsString(), newSettings.get(VALUE).getAsString());
				case "integer":
					return settingValueFactory.newIntegerSettingValue(newSettings.get(KEY).getAsString(), newSettings.get(VALUE).getAsString());
				case "multilingual":
					return settingValueFactory.newMultiLingualStringSettingValue(newSettings.get(KEY).getAsString(), newSettings.get(VALUE).getAsString());
				case "string":
					return settingValueFactory.newStringSettingValue(newSettings.get(KEY).getAsString(), newSettings.get(VALUE).getAsString());
				default:
					throw new UnsupportedOperationException();
			}
		}).orElseThrow(RuntimeException::new);
	}
}