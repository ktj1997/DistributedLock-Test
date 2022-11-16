package com.example.lockstudy.mysql.named;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NamedLockProcessor implements Consumer<Long> {

  private final NamedService service;

  /**
   * Service 내부에 @NamedLock을 걸 수 없는 이유는,
   *
   * @Transcational은 AOP로 동작되는데, AOP가 걸린 Bean에 AOP를 걸 수 없기 떄문이다. (NamedLcckAspect가 원본객체를 바라보고, 그에따라
   * @Transcational이 유지되지 않는다.)
   */
  @Override
  @NamedLock(keyPrefix = "named", identifier = "id", timeOut = 3000)
  public void accept(Long id) {
    service.decrease(id);
  }
}
