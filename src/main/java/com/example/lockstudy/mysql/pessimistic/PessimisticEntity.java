package com.example.lockstudy.mysql.pessimistic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "pessimistic")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PessimisticEntity {

  @Id
  @GeneratedValue
  private Long id;

  private int counter;

  public void increase() {
    counter++;
  }

}
