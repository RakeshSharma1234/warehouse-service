package com.company.warehouseservice.runners;

import com.company.warehouseservice.listeners.SensorDataListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * CommandLineRunner implementation to start the SensorDataListener.
 */
@Component
public class SensorDataListenerRunner implements CommandLineRunner {

  private final SensorDataListener sensorDataListener;

  /**
   * Constructs a new SensorDataListenerRunner with the specified SensorDataListener.
   *
   * @param sensorDataListener the sensor data listener to start
   */
  public SensorDataListenerRunner(SensorDataListener sensorDataListener) {
    this.sensorDataListener = sensorDataListener;
  }

  @Override
  public void run(String... args) {
    sensorDataListener.startListening();
  }
}