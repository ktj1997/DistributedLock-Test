package com.example.lockstudy.mysql.pessimistic;


import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.PessimisticLockScope;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface PessimisticRepository extends JpaRepository<PessimisticEntity, Long> {

  /**
   * InnoDB 사용시 Lock은 인덱스 조건이 걸린 Colum을 기준으로 해야한다. 그렇지 않으면 InnoDB가 Table Lock을 걸 수 있다.
   * <p>
   * MySQL + JPA 에서
   *
   * @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})는 동작 하지않으며,
   * @QueryHints({@QueryHint(name = "javax.persistence.query.timeout", value = "1000")})으로
   * 설정해주어야한다.
   * <p>
   * Lock의 Scope의 경우도 @QueryHint로 지정해 줄 수 있다.
   * @QueryHint(name = "javax.persistence.lock.scope", value = "NORMAL") --> 해당 Entity만 Lock한다.
   * @QueryHint(name = "javax.persistence.lock.scope", value = "EXTENDED") --> 해당 Entity만 연관관계의
   * Entity까지 Lock한다.
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT p FROM PessimisticEntity  p WHERE p.id = :id")
  @QueryHints({
      @QueryHint(name = "javax.persistence.query.timeout", value = "1000"),
      @QueryHint(name = "javax.persistence.lock.scope", value = "NORMAL")})
  Optional<PessimisticEntity> findEntityWithPessimisticLock(@Param("id") Long id);


}
