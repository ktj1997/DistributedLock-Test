package com.example.lockstudy.mysql.optimistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptimisticRepository extends JpaRepository<OptimisticEntity, Long> {
}
