package org.n52.faroe.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is responsible for registering, listing & deleting new services from faroe instance
 */

@RestController
@RequestMapping({"/services"})
public class ServicesController {

  private static final Logger LOG = LoggerFactory.getLogger(ServicesController.class);

  @GetMapping
  public ResponseEntity<Object> getServices() {
    LOG.info("Getting Services");
    try {
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      LOG.error("Couldn't fetch list of settings");
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<Object> getServiceById(@PathVariable("id") Long id) {
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void updateService(@PathVariable( "id" ) Long id, @NonNull @RequestBody Object object) {

  }
}
