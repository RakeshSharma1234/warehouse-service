package com.company.warehouseservice.listeners;

import com.company.warehouseservice.configs.ApplicationProperties;
import com.company.warehouseservice.constants.Messages;
import com.company.warehouseservice.enums.SensorType;
import com.company.warehouseservice.factories.DatagramSocketFactory;
import com.company.warehouseservice.publishers.SensorDataPublisher;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service that listens for UDP sensor data and publishes it to a Kafka topic.
 */
@Service
public class UdpSensorDataListener implements SensorDataListener {
  private static final Logger logger = LoggerFactory.getLogger(UdpSensorDataListener.class);
  private static final int BUFFER_SIZE = 1024;
  private final SensorDataPublisher sensorDataPublisher;
  private final ApplicationProperties applicationProperties;
  private final ExecutorService executorService;

  private final DatagramSocketFactory datagramSocketFactory;

  /**
   * Constructs a new UdpSensorDataListener with the specified SensorDataPublisher and ApplicationProperties.
   * Initializes an ExecutorService with two threads to handle listening for temperature and humidity data.
   *
   * @param sensorDataPublisher   the service responsible for publishing sensor data
   * @param applicationProperties the application properties containing configuration values such as port numbers
   * @param datagramSocketFactory the factory class object for creating instances of DatagramSocket.
   */
  public UdpSensorDataListener(SensorDataPublisher sensorDataPublisher, ApplicationProperties applicationProperties,
                               DatagramSocketFactory datagramSocketFactory) {
    this.sensorDataPublisher = sensorDataPublisher;
    this.applicationProperties = applicationProperties;
    this.datagramSocketFactory = datagramSocketFactory;
    this.executorService = Executors.newFixedThreadPool(2);
  }

  /**
   * Starts listening for sensor data on the configured ports.
   */
  @Override
  public void startListening() {
    listenForData(applicationProperties.getTemperaturePort(), SensorType.TEMPERATURE);
    listenForData(applicationProperties.getHumidityPort(), SensorType.HUMIDITY);
  }

  /**
   * Listens for sensor data on the specified port and publishes it.
   *
   * @param port       the port to listen on for incoming UDP packets
   * @param sensorType the type of sensor data being listened for (e.g., Temperature or Humidity)
   */
  private void listenForData(int port, SensorType sensorType) {
    executorService.submit(() -> {
      try (DatagramSocket socket = datagramSocketFactory.createSocket(port)) {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (!Thread.currentThread().isInterrupted()) {
          socket.receive(packet);
          String sensorData = new String(packet.getData(), 0, packet.getLength());
          sensorDataPublisher.publishSensorData(sensorType, sensorData);
        }
      } catch (SocketException e) {
        logger.error(String.format(Messages.SOCKET_ERROR_FOR_PORT, port), e);
      } catch (IOException e) {
        logger.error(String.format(Messages.IO_ERROR_FOR_PORT, port), e);
      }
    });
  }

  /**
   * Shuts down the listener service.This method is called before the application is shut down to clean up resources.
   * It shuts down the executor service to terminate any running listener threads and logs the shutdown event.
   */
  @PreDestroy
  public void shutdown() {
    // Shutdown the executor service to clean up threads
    executorService.shutdownNow();
    logger.info(Messages.LISTENER_HAS_BEEN_SHUT_DOWN);
  }
}