package com.company.warehouseservice.configs;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for application properties related to Kafka and sensor settings.
 * This class maps application configuration properties from
 * the `application.properties` file to Java fields.
 */
@Configuration
@Getter
public class ApplicationProperties {
  /**
   * The Kafka bootstrap servers.
   */
  @Value("${spring.kafka.producer.bootstrap-servers}")
  private String bootstrapServers;

  /**
   * The Kafka key deserializer class.
   */
  @Value("${spring.kafka.producer.key-serializer}")
  private String keySerializer;

  /**
   * The Kafka value deserializer class.
   */
  @Value("${spring.kafka.producer.value-serializer}")
  private String valueSerializer;

  /**
   * The UDP port for reading the data from temperature sensor.
   */
  @Value("${sensor.temperature.port}")
  private int temperaturePort;

  /**
   * The UDP port for reading the data from humidity sensor.
   */
  @Value("${sensor.humidity.port}")
  private int humidityPort;

  /**
   * The Kafka topic for sensor data.
   */
  @Value("${kafka.topic.sensor-data}")
  private String kafkaTopic;

}