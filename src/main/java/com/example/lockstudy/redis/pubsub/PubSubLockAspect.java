package com.example.lockstudy.redis.pubsub;

import com.example.lockstudy.mysql.named.LockRepository;
import com.example.lockstudy.mysql.named.NamedLock;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Connection;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Order(value = Ordered.LOWEST_PRECEDENCE - 1)
public class PubSubLockAspect {

  private final RedissonClient redissonClient;


  @Around("@annotation(pubSubLock)")
  public Object processPubSubLock(ProceedingJoinPoint pjp, PubSubLock pubSubLock) throws Throwable {
    Object result = null;
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    Object[] args = pjp.getArgs();

    String lockKey = pubSubLock.keyPrefix() + getIdentifier(pubSubLock.identifier(), method, args);
    RLock lock = redissonClient.getLock(lockKey);
    if (lock.tryLock(pubSubLock.timeOut(), pubSubLock.leaseTime(), TimeUnit.MILLISECONDS)) {
      try {
        result = pjp.proceed();
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException(e.getMessage());
      } finally {
        lock.unlock();
      }
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
