package com.company.warehouseservice.publishers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.company.warehouseservice.configs.ApplicationProperties;
import com.company.warehouseservice.enums.SensorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;

class KafkaSensorDataPublisherTest {

  @Mock
  private KafkaSender<String, String> kafkaSender;

  @Mock
  private ApplicationProperties applicationProperties;

  private KafkaSensorDataPublisher kafkaSensorDataPublisher;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    when(applicationProperties.getKafkaTopic()).thenReturn("test-topic");
    kafkaSensorDataPublisher = new KafkaSensorDataPublisher(kafkaSender, applicationProperties);
  }

  @Test
  void testPublishSensorData() {
    // Given
    SensorType sensorType = SensorType.TEMPERATURE;
    String sensorData = "sensorId=123;value=25.5";

    // Mocking the behavior of kafkaSender
    when(kafkaSender.send(any(Mono.class))).thenReturn(Flux.empty());

    // Act
    kafkaSensorDataPublisher.publishSensorData(sensorType, sensorData);

    // Assert
    verify(kafkaSender, times(1)).send(any(Mono.class));
  }
}