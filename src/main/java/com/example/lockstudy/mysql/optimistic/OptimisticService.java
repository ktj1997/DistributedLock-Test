package com.example.lockstudy.mysql.optimistic;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OptimisticService implements Consumer<Long> {

  private final decreaseProcessor decreaseProcessor;

  /**
   * 실행측의 트랜잭션과 분리해야한다. 재시도를 같은 트랜잭션에서 실행하게 되면 RepetableRead로 인해서, 새롭게 갱신된 Version을 읽어오지 못하게된다.
   */
  @Override
  public void accept(Long id) {
    while (true) {
      try {
        decreaseProcessor.increase(id);
        break;
      } catch (ObjectOptimisticLockingFailureException ex) {
        System.out.println("Conflict Occurred");
      } catch (RuntimeException ex) {
        System.out.println(ex.getMessage());
        break;
      }
    }
  }
}
