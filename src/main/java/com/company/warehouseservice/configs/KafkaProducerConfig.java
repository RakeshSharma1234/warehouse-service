package com.company.warehouseservice.configs;

import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

/**
 * This class sets up the necessary configurations for producing messages to Kafka topics using reactive Kafka sender.
 * It reads configuration properties from ApplicationProperties.
 */
@Configuration
public class KafkaProducerConfig {

  private final ApplicationProperties applicationProperties;

  /**
   * Constructs a KafkaProducerConfig instance with the provided application properties.
   *
   * @param applicationProperties the application properties containing Kafka producer configurations
   */
  public KafkaProducerConfig(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  /**
   * Creates and configures SenderOptions bean for Kafka producer.
   *
   * @return the configured SenderOptions
   */
  @Bean
  public SenderOptions<String, String> senderOptions() {
    return SenderOptions.create(
        Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationProperties.getBootstrapServers(),
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, applicationProperties.getKeySerializer(),
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, applicationProperties.getValueSerializer()
        )
    );
  }

  /**
   * This method initializes the Kafka sender which is used to send messages to Kafka topics.
   *
   * @param senderOptions the sender options to be used for the Kafka sender
   * @return the configured KafkaSender
   */
  @Bean
  public KafkaSender<String, String> kafkaSender(SenderOptions<String, String> senderOptions) {
    return KafkaSender.create(senderOptions);
  }
}