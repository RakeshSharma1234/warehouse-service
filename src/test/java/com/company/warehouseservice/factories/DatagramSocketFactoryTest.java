package com.company.warehouseservice.factories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.net.DatagramSocket;
import java.net.SocketException;

class DatagramSocketFactoryTest {

  private final DatagramSocketFactory factory = new DatagramSocketFactory();

  @Test
  void testCreateSocketWithValidPort() {
    int port = 12345;
    try {
      DatagramSocket socket = factory.createSocket(port);
      assertNotNull(socket, "DatagramSocket should not be null");
      assertEquals(port, socket.getLocalPort(), "The DatagramSocket should be bound to the specified port");
      socket.close(); // Clean up
    } catch (SocketException e) {
      fail("SocketException should not be thrown for a valid port");
    }
  }

  @Test
  void testCreateSocketWithInvalidPort() {
    int invalidPort = -1; // Invalid port
    assertThrows(IllegalArgumentException.class, () -> {
      factory.createSocket(invalidPort);
    }, "IllegalArgumentException should be thrown for an invalid port");
  }

  @Test
  void testCreateSocketErrorHandling() {
    int port = 65535; // Typically, this port is valid, but we can test conflict by creating multiple sockets

    try {
      DatagramSocket socket1 = factory.createSocket(port);
      DatagramSocket socket2 = factory.createSocket(port); // This may cause a conflict

      // If this point is reached, the second socket was created successfully, which is unexpected
      fail("SocketException should be thrown due to port conflict or system limits");
    } catch (SocketException e) {
      // Expected exception due to port conflict or system limitations
    }
  }
}