package com.company.warehouseservice.publishers;

import com.company.warehouseservice.configs.ApplicationProperties;
import com.company.warehouseservice.enums.SensorType;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

/**
 * Implementation responsible for publishing sensor data to Kafka.
 */
@Service
public class KafkaSensorDataPublisher implements SensorDataPublisher {

  private final KafkaSender<String, String> kafkaSender;
  private final ApplicationProperties applicationProperties;

  /**
   * Constructs a new KafkaSensorDataPublisher with the specified KafkaSender and ApplicationProperties.
   *
   * @param kafkaSender           the Kafka sender for publishing messages
   * @param applicationProperties the application properties containing Kafka configurations
   */
  public KafkaSensorDataPublisher(KafkaSender<String, String> kafkaSender, ApplicationProperties applicationProperties) {
    this.kafkaSender = kafkaSender;
    this.applicationProperties = applicationProperties;
  }

  /**
   * Publishes the given sensor data to Kafka.
   *
   * @param sensorType the type of the sensor
   * @param sensorData the data from the sensor
   */
  @Override
  public void publishSensorData(SensorType sensorType, String sensorData) {
    String payload = String.format("type=%s;%s", sensorType.name(), sensorData);
    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(applicationProperties.getKafkaTopic(), null, payload);
    kafkaSender.send(Mono.just(SenderRecord.create(producerRecord, null))).subscribe();
  }
}