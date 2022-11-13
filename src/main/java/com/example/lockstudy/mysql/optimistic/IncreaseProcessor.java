package com.example.lockstudy.mysql.optimistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class IncreaseProcessor {

  private final OptimisticRepository optimisticRepository;

  @Transactional
  public void increase(Long id) {
    OptimisticEntity entity = optimisticRepository.findById(id).orElseThrow(RuntimeException::new);
    entity.increase();
    ;
  }
}
