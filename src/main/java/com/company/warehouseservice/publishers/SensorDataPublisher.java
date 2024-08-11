package com.company.warehouseservice.publishers;

import com.company.warehouseservice.enums.SensorType;

/**
 * Interface for publishing sensor data.
 */
public interface SensorDataPublisher {

  /**
   * Publishes the given sensor data.
   *
   * @param sensorType the type of the sensor
   * @param sensorData the data from the sensor
   */
  void publishSensorData(SensorType sensorType, String sensorData);
}