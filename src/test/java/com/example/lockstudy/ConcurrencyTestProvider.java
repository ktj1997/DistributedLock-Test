package com.example.lockstudy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.springframework.util.StopWatch;

public class ConcurrencyTestProvider {

  public void test(Long id, int count, Consumer<Long> processor) throws InterruptedException {
    ExecutorService concurrencyService = Executors.newFixedThreadPool(32);
    CountDownLatch latch = new CountDownLatch(count);

    for (int i = 0; i < count; i++) {
      concurrencyService.submit(() -> {
        try {
          processor.accept(1L);
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
  }
}
