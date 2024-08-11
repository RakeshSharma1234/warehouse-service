package com.company.warehouseservice.runners;

import com.company.warehouseservice.listeners.SensorDataListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SensorDataListenerRunnerTest {

  @Mock
  private SensorDataListener sensorDataListener;

  private CommandLineRunner sensorDataListenerRunner;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    sensorDataListenerRunner = new SensorDataListenerRunner(sensorDataListener);
  }

  @Test
  void testRun() throws Exception {
    // Arrange
    String[] args = {};

    // Act
    sensorDataListenerRunner.run(args);

    // Assert
    verify(sensorDataListener, times(1)).startListening();
  }
}