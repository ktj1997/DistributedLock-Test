package com.example.lockstudy.redis.spin;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpinLockService implements Consumer<Long> {

  private final RedisSpinLockRepository lockRepository;
  private final SpinDecreaseProcessor processor;

  @Override
  public void accept(Long id) {
    String key = "prefix" + id;
    int timeOut = 3000;
    while (!lockRepository.getLock(key, timeOut)) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      processor.decrease(id);
    } finally {
      lockRepository.releaseLock(key);
    }
  }
}
