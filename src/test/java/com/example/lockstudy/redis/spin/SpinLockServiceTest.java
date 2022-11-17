package com.example.lockstudy.redis.spin;

import com.example.lockstudy.ConcurrencyTestProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpinLockServiceTest {

  @Autowired
  private SpinLockService service;

  @Autowired
  private SpinRepository repository;

  @Test
  public void decreaseInRaceCondition() throws InterruptedException {
    ConcurrencyTestProvider provider = new ConcurrencyTestProvider();
    provider.test(1L, 100, service);

    SpinEntity entity = repository.findById(1L).orElseThrow(RuntimeException::new);

    Assertions.assertEquals(0, entity.getCounter());
  }

}