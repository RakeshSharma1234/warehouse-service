package com.company.warehouseservice.factories;

import org.springframework.stereotype.Component;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * A factory class for creating instances of DatagramSocket.
 */
@Component
public class DatagramSocketFactory {

  /**
   * Creates a new DatagramSocket bound to the specified port.
   *
   * @param port the port to which the DatagramSocket should be bound
   * @return a new DatagramSocket instance bound to the specified port
   * @throws SocketException if an error occurs while creating the socket
   */
  public DatagramSocket createSocket(int port) throws SocketException {
    return new DatagramSocket(port);
  }
}