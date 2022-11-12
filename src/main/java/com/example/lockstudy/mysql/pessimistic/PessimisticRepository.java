package com.example.lockstudy.mysql.pessimistic;


import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PessimisticRepository extends JpaRepository<PessimisticEntity, Long> {

  /**
   * MySQL + JPA 에서
   * @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})는 동작 하지않으며,
   * @QueryHints({@QueryHint(name = "javax.persistence.query.timeout", value = "1000")})으로 설정해주어야 한다.
   *
   * InnoDB 사용시 Lock은 인덱스 조건이 걸린 Colum을 기준으로 해야한다.
   * 그렇지 않으면 InnoDB가 Table Lock을 걸 수 있다.
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Transactional
  @Query("SELECT p FROM PessimisticEntity  p WHERE p.id = :id")
  @QueryHints({@QueryHint(name = "javax.persistence.query.timeout", value = "1000")})
  Optional<PessimisticEntity> findPessimisticEntitiesWithLock(@Param("id") Long id) throws QueryTimeoutException;


}
