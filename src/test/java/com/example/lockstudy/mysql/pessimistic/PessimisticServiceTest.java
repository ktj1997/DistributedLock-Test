package com.example.lockstudy.mysql.pessimistic;

import com.example.lockstudy.ConcurrencyTestProvider;
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
    int countExpect = 200;
    ConcurrencyTestProvider tester = new ConcurrencyTestProvider();
    tester.test(1L, countExpect, service);

    Assertions.assertEquals(countExpect, repository.findById(1L).get().getCounter());

  }
}