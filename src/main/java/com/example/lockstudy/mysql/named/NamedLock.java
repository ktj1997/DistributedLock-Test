package com.example.lockstudy.mysql.named;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NamedLock {

  String keyPrefix() default "";
  String identifier() default "";
  int timeOut() default 2000;

}
