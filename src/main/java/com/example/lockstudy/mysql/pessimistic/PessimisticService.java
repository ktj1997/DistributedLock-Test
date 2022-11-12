package com.example.lockstudy.mysql.pessimistic;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PessimisticService implements Consumer<Long> {

  private final PessimisticRepository pessimisticRepository;

  @Override
  @Transactional
  public void accept(Long id) {
    try {
      PessimisticEntity entity = pessimisticRepository.findPessimisticEntitiesWithLock(id)
          .orElseThrow(RuntimeException::new);
      System.out.println("Entity's count : " + entity.getCounter());
      Thread.sleep(100);
      entity.increase();
      System.out.println("Entity's count : " + entity.getCounter());
    }catch (InterruptedException e){
      System.out.println("Thread Exception");
    }catch (QueryTimeoutException e){
      System.out.println("Lock TimeOut");
    }
  }
}
