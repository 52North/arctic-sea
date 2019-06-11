package org.n52.faroe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Starting point for the faroe API.
 */
@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
public class Main {

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}