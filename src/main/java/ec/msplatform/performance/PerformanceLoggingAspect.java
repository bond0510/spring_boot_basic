package ec.msplatform.performance;

import java.lang.reflect.Method;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${performance.logging.enabled:true}")
public class PerformanceLoggingAspect {

  @Around("execution(* ec.msplatform.service..*(..))|| "
      + "execution(* ec.msplatform.logic..*(..)) || "
      + "execution(* ec.msplatform.dataaccess..*(..)) || "
      + "execution(* ec.msplatform.kafkamodule..*(..)) || "
      + "execution(* ec.msplatform.logging..*(..))")

  public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

    MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

    String name = createInvocationTraceName(methodSignature);
    final StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    try {
      return proceedingJoinPoint.proceed();
    } finally {
      stopWatch.stop();
      log.info(String.format("Method %s execution took %dms (%s)", name, stopWatch.getTotalTimeMillis(),
          DurationFormatUtils.formatDurationWords(stopWatch.getTotalTimeMillis(), true, true)));
    }
  }

  private String createInvocationTraceName(MethodSignature methodSignature) {

    Method method = methodSignature.getMethod();
    Class<?> clazz = method.getDeclaringClass();
    String className = clazz.getName();
    return className + '.' + method.getName();
  }
}
