package com.example.lockstudy.redis.pubsub;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "pubsub")
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public class PubSubEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int counter;

  public void decrease() {
    if (counter == 0) {
      throw new RuntimeException("Not Enough");
    }
    counter--;
  }

}
