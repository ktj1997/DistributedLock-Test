package com.example.lockstudy.mysql.named;

import java.sql.Connection;
import java.util.function.Consumer;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class NamedLockProcessor implements Consumer<Long> {

  private final DataSource lockDataSource;

  private final LockRepository lockRepository;
  private final NamedService service;

  public NamedLockProcessor(
      @Qualifier(value = "lockDataSource")
      DataSource lockDataSource,
      NamedService namedService,
      LockRepository lockRepository) {
    this.lockDataSource = lockDataSource;
    this.service = namedService;
    this.lockRepository = lockRepository;
  }


  @Override
  public void accept(Long id) {
    String LOCK_KEY = "NamedLock";
    try (Connection conn = lockDataSource.getConnection();) {
      lockRepository.getLock(conn, LOCK_KEY, 2000);
      service.decrease(1L);
      lockRepository.releaseLock(conn, LOCK_KEY);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
