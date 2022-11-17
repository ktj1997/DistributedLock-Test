package com.example.lockstudy.redis.spin;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisSpinLockRepository {

  private final RedisTemplate<String, String> redisTemplate;

  public Boolean getLock(String key, int timeout) {
    return redisTemplate.opsForValue()
        .setIfAbsent(key, "SpinLock", Duration.ofMillis(timeout));
  }

  public Boolean releaseLock(String key) {
    return redisTemplate.delete(key);
  }

}
