package com.example.lockstudy.mysql.pessimistic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

@SpringBootTest
class PessimisticServiceTest {

  @Autowired
  private PessimisticRepository repository;

  @Autowired
  private PessimisticService service;

  @Test
  public void decreaseInRaceCondition() throws InterruptedException {
    StopWatch stopWatch = new StopWatch();
    ExecutorService concurrencyService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(200);
    stopWatch.start();

    for (int i = 0; i < 200; i++) {
      concurrencyService.submit(() -> {
        try {
          Assertions.assertDoesNotThrow(() -> service.accept(1L));
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    stopWatch.stop();
    System.out.println(stopWatch.prettyPrint());

    Assertions.assertEquals(200, repository.findById(1L).get().getCounter());
  }
}