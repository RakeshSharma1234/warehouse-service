package com.company.warehouseservice.configs;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class KafkaProducerConfigTest {

  @Mock
  private ApplicationProperties applicationProperties;

  private KafkaProducerConfig kafkaProducerConfig;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    when(applicationProperties.getBootstrapServers()).thenReturn("localhost:9092");
    when(applicationProperties.getKeySerializer()).thenReturn("org.apache.kafka.common.serialization.StringSerializer");
    when(applicationProperties.getValueSerializer()).thenReturn("org.apache.kafka.common.serialization.StringSerializer");
    kafkaProducerConfig = new KafkaProducerConfig(applicationProperties);
  }

  @Test
  void testSenderOptions() {
    // Act
    SenderOptions<String, String> senderOptions = kafkaProducerConfig.senderOptions();

    // Assert
    assertNotNull(senderOptions);
    Map<String, Object> config = senderOptions.producerProperties();
    assertEquals("localhost:9092", config.get(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
    assertEquals("org.apache.kafka.common.serialization.StringSerializer", config.get(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
    assertEquals("org.apache.kafka.common.serialization.StringSerializer", config.get(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
  }

  @Test
  void testKafkaSender() {
    SenderOptions<String, String> senderOptions = kafkaProducerConfig.senderOptions();
    KafkaSender<String, String> kafkaSender = kafkaProducerConfig.kafkaSender(senderOptions);

    // Assert
    assertNotNull(kafkaSender);
  }
}