package com.example.lockstudy.redis.pubsub;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PubSubRepository extends JpaRepository<PubSubEntity, Long> {

}
