package com.example.lockstudy.mysql.named;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NamedService {

  private final NamedRepository namedRepository;

  @Transactional
  public void decrease(Long aLong) {
    NamedLockEntity entity = namedRepository.findById(1L).orElseThrow(RuntimeException::new);
    entity.decrease();
  }
}
