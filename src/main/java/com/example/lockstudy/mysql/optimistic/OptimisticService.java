package com.example.lockstudy.mysql.optimistic;

import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OptimisticService implements Consumer<Long> {

  private final IncreaseProcessor increaseProcessor;

  /**
   * 실행측의 트랜잭션과 분리해야한다.
   * 재시도를 같은 트랜잭션에서 실행하게 되면
   * RepetableRead로 인해서, 새롭게 갱신된 Version을 읽어오지 못하게된다.
   */
  @Override
  public void accept(Long id) {
    while (true) {
      try {
        increaseProcessor.increase(id);
        break;
      } catch (ObjectOptimisticLockingFailureException ex) {
          System.out.println("Conflict Occurred");
      }
    }
  }
}
