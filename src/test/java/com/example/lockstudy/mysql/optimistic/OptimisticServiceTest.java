package com.example.lockstudy.mysql.optimistic;

import com.example.lockstudy.ConcurrencyTestProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class OptimisticServiceTest {

  @Autowired
  private OptimisticRepository repository;

  @Autowired
  private OptimisticService service;

  @Test
  public void increaseInRaceCondition() throws InterruptedException {
    ConcurrencyTestProvider provider = new ConcurrencyTestProvider();
    provider.test(1L, 100, service);

    Assertions.assertEquals(0, repository.findById(1L).get().getCounter());
  }

}