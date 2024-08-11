package com.company.warehouseservice.listeners;

/**
 * Interface for listening to sensor data.
 */
public interface SensorDataListener {
  /**
   * Starts listening for sensor data on configured ports.
   */
  void startListening();
}