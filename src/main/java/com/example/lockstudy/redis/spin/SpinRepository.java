package com.example.lockstudy.redis.spin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpinRepository extends JpaRepository<SpinEntity, Long> {

}
