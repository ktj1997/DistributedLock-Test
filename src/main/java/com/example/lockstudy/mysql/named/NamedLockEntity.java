package com.example.lockstudy.mysql.named;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "named")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NamedLockEntity {

  @Id
  @GeneratedValue
  private Long id;

  private int counter;

  public void decrease() {
    if (counter == 0) {
      throw new RuntimeException("Not Enough");
    }
    counter--;
  }

  public void increase() {
    counter++;
  }

}
