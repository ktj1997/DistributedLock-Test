package com.example.lockstudy.redis.pubsub;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PubSubService implements Consumer<Long> {

  private final PubSubDecreaseProcessor processor;

  @Override
  @PubSubLock(keyPrefix = "pubsub", identifier = "aLong", leaseTime = 1000, timeOut = 2000)
  public void accept(Long aLong) {
    processor.decrease(aLong);
  }
}
