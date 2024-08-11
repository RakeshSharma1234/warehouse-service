package com.company.warehouseservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WarehouseServiceApplicationTests {
  @Test
  void contextLoads() {
  }

  /**
   * Test to ensure that the main method starts the application without exceptions.
   */
  @Test
  void testMain() {
    WarehouseServiceApplication.main(new String[] {});
    Assertions.assertThatNoException();
    // If the application fails to start, this test will fail
  }
}