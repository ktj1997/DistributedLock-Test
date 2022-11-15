package com.example.lockstudy.mysql.named;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NamedRepository extends JpaRepository<NamedLockEntity, Long> {

}
