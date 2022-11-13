package com.example.lockstudy.mysql.optimistic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "optimistic")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptimisticEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private int counter;

  @Version
  private Long version;

  public void increase() {
    counter++;
  }

}
