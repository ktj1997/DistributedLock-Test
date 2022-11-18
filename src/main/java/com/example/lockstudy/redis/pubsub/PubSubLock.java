package com.example.lockstudy.redis.pubsub;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PubSubLock {

  String keyPrefix();

  String identifier();

  int leaseTime();

  int timeOut();

}
