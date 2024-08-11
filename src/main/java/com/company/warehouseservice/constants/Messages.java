package com.company.warehouseservice.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A constant class that holds message templates used throughout the application.
 * It is designed to be used by other classes for consistent message formatting.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {
  /**
   * Message template for indicating socket error for sensor port.
   */
  public static final String SOCKET_ERROR_FOR_PORT = "Socket error while listening on port: %d ";

  /**
   * Message template for indicating IO error for sensor port.
   */
  public static final String IO_ERROR_FOR_PORT = "I/O error while listening on port: %d";

  /**
   * Message template for indicating UDP Listener has been shut down.
   */
  public static final String LISTENER_HAS_BEEN_SHUT_DOWN = "UdpSensorDataListener has been shut down.";

}
