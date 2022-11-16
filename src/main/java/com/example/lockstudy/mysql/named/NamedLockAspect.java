package com.example.lockstudy.mysql.named;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import javax.sql.DataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class NamedLockAspect {

  private final ApplicationContext acc;
  private final DataSource lockDataSource;

  private final LockRepository lockRepository;

  public NamedLockAspect(
      @Qualifier(value = "lockDataSource")
      DataSource lockDataSource,
      LockRepository lockRepository,
      ApplicationContext acc) {
    this.lockDataSource = lockDataSource;
    this.lockRepository = lockRepository;
    this.acc = acc;
  }

  @Around("@annotation(namedLock)")
  public Object processNamedLock(ProceedingJoinPoint pjp, NamedLock namedLock) {
    if (namedLock.identifier().isEmpty()) {
      throw new RuntimeException("NamedLock Identifier is Empty");
    }
    Object result = null;
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    Object[] args = pjp.getArgs();

    String lockKey = namedLock.keyPrefix() + getIdentifier(namedLock.identifier(), method, args);
    try (Connection conn = lockDataSource.getConnection();) {
      lockRepository.getLock(conn, lockKey, namedLock.timeOut());
      result = pjp.proceed();
      lockRepository.releaseLock(conn, lockKey);
    } catch (Throwable ex) {
      ex.printStackTrace();
    }
    return result;
  }

  private String getIdentifier(String identifier, Method method, Object[] args) {
    Parameter[] parameters = method.getParameters();
    for (int i = 0; i < parameters.length; i++) {
      if (parameters[i].getName().equals(identifier)) {
        return args[i].toString();
      }
    }
    throw new RuntimeException("There is no parameter [" + identifier + "]");
  }
}
