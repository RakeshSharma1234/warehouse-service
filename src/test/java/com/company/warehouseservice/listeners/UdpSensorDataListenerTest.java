package com.company.warehouseservice.listeners;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Logger;
import com.company.warehouseservice.commons.TestAppender;
import com.company.warehouseservice.configs.ApplicationProperties;
import com.company.warehouseservice.constants.Messages;
import com.company.warehouseservice.enums.SensorType;
import com.company.warehouseservice.factories.DatagramSocketFactory;
import com.company.warehouseservice.publishers.SensorDataPublisher;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

class UdpSensorDataListenerTest {
  @Mock
  private SensorDataPublisher sensorDataPublisher;

  @Mock
  private ApplicationProperties applicationProperties;

  @Mock
  private DatagramSocketFactory datagramSocketFactory;

  @InjectMocks
  private UdpSensorDataListener udpSensorDataListener;

  private TestAppender testAppender;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(applicationProperties.getTemperaturePort()).thenReturn(0);
    when(applicationProperties.getHumidityPort()).thenReturn(0);
    udpSensorDataListener = new UdpSensorDataListener(sensorDataPublisher, applicationProperties, datagramSocketFactory);
    testAppender = new TestAppender();
    Logger logger = (Logger) LoggerFactory.getLogger(UdpSensorDataListener.class);
    logger.addAppender(testAppender);
    testAppender.start();
  }

  @Test
  void testStartListening() throws Exception {
    // Mock DatagramSocket and DatagramPacket
    DatagramSocket mockSocket = mock(DatagramSocket.class);
    when(datagramSocketFactory.createSocket(anyInt())).thenReturn(mockSocket);

    // Start the listener
    udpSensorDataListener.startListening();

    // Verify that listenForData is called with the correct parameters
    verify(applicationProperties).getTemperaturePort();
    verify(applicationProperties).getHumidityPort();
  }

  @Test
  void testListenForData() throws Exception {
    DatagramSocket mockSocket = mock(DatagramSocket.class);
    when(datagramSocketFactory.createSocket(anyInt())).thenReturn(mockSocket);

    // Simulate receiving data
    doAnswer(invocation -> {
      DatagramPacket packet = invocation.getArgument(0);
      packet.setData("Test data".getBytes());
      return null;
    }).when(mockSocket).receive(any(DatagramPacket.class));

    // Start the listener in a separate thread
    Thread listenerThread = new Thread(() -> udpSensorDataListener.startListening());
    listenerThread.start();

    Thread.sleep(100);
    // Verify that the data was published
    verify(sensorDataPublisher, atLeastOnce()).publishSensorData(SensorType.TEMPERATURE, "Test data");

    // Clean up
    listenerThread.interrupt();
    listenerThread.join();
  }

  @Test
  void testHandleSocketException() throws Exception {
    DatagramSocket mockSocket = mock(DatagramSocket.class);
    when(datagramSocketFactory.createSocket(anyInt())).thenReturn(mockSocket);
    doThrow(new SocketException("Socket Error")).when(mockSocket).receive(any(DatagramPacket.class));
    // Start the listener
    udpSensorDataListener.startListening();
    Thread.sleep(100);
    assertFalse(testAppender.getEvents().isEmpty());
    assertTrue(testAppender.getEvents().get(0).getFormattedMessage().contains(String.format(Messages.SOCKET_ERROR_FOR_PORT, 0)));
    assertTrue(testAppender.getEvents().get(1).getFormattedMessage().contains(String.format(Messages.SOCKET_ERROR_FOR_PORT, 0)));

  }

  @Test
  void testHandleIOException() throws Exception {
    DatagramSocket mockSocket = mock(DatagramSocket.class);
    when(datagramSocketFactory.createSocket(anyInt())).thenReturn(mockSocket);
    doThrow(new IOException("IO Error")).when(mockSocket).receive(any(DatagramPacket.class));

    // Start the listener
    udpSensorDataListener.startListening();

    Thread.sleep(100);
    assertFalse(testAppender.getEvents().isEmpty());
    assertTrue(testAppender.getEvents().get(0).getFormattedMessage().contains(String.format(Messages.IO_ERROR_FOR_PORT, 0)));
    assertTrue(testAppender.getEvents().get(1).getFormattedMessage().contains(String.format(Messages.IO_ERROR_FOR_PORT, 0)));

  }

  @Test
  void testShutdown() {
    udpSensorDataListener.shutdown();
    // Verify that exceptions are logged
    // Check the logs
    assertEquals(1, testAppender.getEvents().size());
    assertTrue(testAppender.getEvents().get(0).getFormattedMessage().contains(Messages.LISTENER_HAS_BEEN_SHUT_DOWN));
  }
}