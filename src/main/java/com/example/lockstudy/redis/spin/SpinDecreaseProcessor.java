package com.example.lockstudy.redis.spin;

import javax.management.relation.RelationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpinDecreaseProcessor {

  private final SpinRepository repository;

  @Transactional
  public void decrease(Long id) {
    SpinEntity entity = repository.findById(id).orElseThrow(RuntimeException::new);
    entity.decrease();
  }
}
