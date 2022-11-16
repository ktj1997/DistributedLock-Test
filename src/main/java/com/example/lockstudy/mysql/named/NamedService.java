package com.example.lockstudy.mysql.named;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NamedService implements Consumer<Long> {

  private final NamedRepository namedRepository;

  @Override
  @Transactional
  @NamedLock(keyPrefix = "named", identifier = "aLong", timeOut = 3000)
  public void accept(Long aLong) {
    NamedLockEntity entity = namedRepository.findById(1L).orElseThrow(RuntimeException::new);
    entity.decrease();
  }
}
