package org.n52.faroe.controller;

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

  @GetMapping
  public ResponseEntity<Object> getSettingDefinitons(@PathVariable("service") Long serviceId) {
    LOG.info("Getting definitions");
    try {
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      LOG.error(String.format("Couldn't fetch setting definitions for the id: %d", serviceId));
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Object> getSettingDefinitionById(@PathVariable("service") Long serviceId, @PathVariable("id") String definitionId) {
    LOG.info(String.format("Getting definition for service %d", serviceId));
    try {
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      LOG.error(String.format("Couldn't fetch setting definition with id %s for the service %d", definitionId, serviceId));
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping(value = "/{id}")
  public void updateSettingDefinition(@PathVariable("service") Long serviceId, @PathVariable("id") String definitionId) {

  }
}
