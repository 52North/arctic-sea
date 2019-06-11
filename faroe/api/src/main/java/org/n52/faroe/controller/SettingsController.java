package org.n52.faroe.controller;

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

  @PostMapping
  public ResponseEntity<Object> updateSettings(@PathVariable("service") Long serviceId, @RequestBody String newSettings) {
    LOG.info("Updating Settings");
    try {
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      LOG.error("Update Request Failed");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping
  public ResponseEntity<Object> getSettings(@PathVariable("service") Long serviceId) {
    LOG.info("Getting Settings");
    try {
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      LOG.error("Couldn't fetch settings");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping
  public ResponseEntity<Object> deleteSettings(@PathVariable("service") Long serviceId) {
    LOG.info("Deleting Settings");
    try {
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      LOG.error("Couldn't delete all settings!");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}