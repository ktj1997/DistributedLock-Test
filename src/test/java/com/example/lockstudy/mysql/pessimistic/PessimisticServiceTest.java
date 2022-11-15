package com.example.lockstudy.mysql.pessimistic;

import com.example.lockstudy.ConcurrencyTestProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PessimisticServiceTest {

  @Autowired
  private PessimisticRepository repository;

  @Autowired
  private PessimisticService service;

  @Test
  public void increaseInRaceCondition() throws InterruptedException {
    ConcurrencyTestProvider tester = new ConcurrencyTestProvider();
    tester.test(1L, 100, service);

    Assertions.assertEquals(0, repository.findById(1L).get().getCounter());

  }
}