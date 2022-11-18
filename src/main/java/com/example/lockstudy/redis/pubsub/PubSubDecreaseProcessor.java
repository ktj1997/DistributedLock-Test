package com.example.lockstudy.redis.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PubSubDecreaseProcessor {

  private final PubSubRepository repository;

  @Transactional
  public void decrease(Long id) {
    PubSubEntity entity = repository.findById(id).orElseThrow(RuntimeException::new);
    entity.decrease();
  }

}
