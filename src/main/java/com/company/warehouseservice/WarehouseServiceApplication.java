package com.company.warehouseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Warehouse Service.
 */
@SpringBootApplication
public class WarehouseServiceApplication {
  /**
   * The main method that serves as the entry point of the application.
   *
   * @param args command-line arguments passed to the application
   */
  public static void main(String[] args) {
    SpringApplication.run(WarehouseServiceApplication.class, args);
  }
}