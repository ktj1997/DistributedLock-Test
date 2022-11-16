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

/**
 * 해당 Aspect의 @Order를 지정해준 이유
 *
 * Aspect는 Proxy로, 우선순위가 높다는건 가장 바깥에 있다 는 것이다.
 * 즉, 가장 낮은 우선순위부터 원본객체를 감싼다는 의미이고,
 * @Transcational이 적용된 AOP를 감싸야 하기 떄문에, @EnableTransactionManagement의 Order보다
 * 이 Aspect가 우선순위가 높아야 한다.
 */
@Aspect
@Component
@Order(value = Ordered.LOWEST_PRECEDENCE - 1)
public class NamedLockAspect {

  private final DataSource lockDataSource;

  private final LockRepository lockRepository;

  public NamedLockAspect(
      @Qualifier(value = "lockDataSource")
      DataSource lockDataSource,
      LockRepository lockRepository,
      ApplicationContext acc) {
    this.lockDataSource = lockDataSource;
    this.lockRepository = lockRepository;
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
