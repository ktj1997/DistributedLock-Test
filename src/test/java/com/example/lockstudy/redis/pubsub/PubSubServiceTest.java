package com.example.lockstudy.redis.pubsub;

import com.example.lockstudy.ConcurrencyTestProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PubSubServiceTest {

  @Autowired
  private PubSubService service;

  @Autowired
  private PubSubRepository repository;

  @Test
  public void decreaseInRaceCondition() throws InterruptedException {
    ConcurrencyTestProvider provider = new ConcurrencyTestProvider();
    provider.test(1L, 100, service);

    Assertions.assertEquals(0, repository.findById(1L).get().getCounter());
  }
}