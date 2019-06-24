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

import org.n52.faroe.dao.ServicesDao;
import org.n52.faroe.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

/**
 * This controller is responsible for registering, listing & deleting new services from faroe
 * instance
 */
@RestController
@RequestMapping({"/services"})
public class ServicesController {

	private static final Logger LOG = LoggerFactory.getLogger(ServicesController.class);

	@Inject
	private ServicesDao servicesDao;

	@GetMapping
	public ResponseEntity getServices() {
		LOG.info("Getting Services");
		try {
			List<Service> services = servicesDao.getServices();
			return new ResponseEntity<>(services, HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("Couldn't fetch list of settings");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/{name}")
	public ResponseEntity<Object> getServiceByName(@PathVariable("name") String name) {
		final Service service = servicesDao.getServiceByName(name);
		return new ResponseEntity<>(service, HttpStatus.OK);
	}

	//TODO Find better usage of this
	@PutMapping(value = "/{name}")
	@ResponseStatus(HttpStatus.OK)
	public void updateService(@PathVariable("name") String name,
							  @RequestBody String body) {

	}
}
