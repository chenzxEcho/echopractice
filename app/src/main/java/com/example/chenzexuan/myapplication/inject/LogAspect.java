package com.example.chenzexuan.myapplication.inject;

import com.example.chenzexuan.myapplication.nohook.LogUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/** Aspect representing the cross cutting-concern: Method and Constructor Tracing. */
@Aspect
public class LogAspect {

  private static final String POINTCUT_METHOD =
      "execution(@com.example.chenzexuan.myapplication.inject.InjectLog * *(..))";

  private static final String POINTCUT_CONSTRUCTOR =
      "execution(@com.example.chenzexuan.myapplication.inject.InjectLog *.new(..))";

  @Pointcut(POINTCUT_METHOD)
  public void methodAnnotatedWithDebugTrace() {}

  @Pointcut(POINTCUT_CONSTRUCTOR)
  public void constructorAnnotatedDebugTrace() {}

  @Around("methodAnnotatedWithDebugTrace() || constructorAnnotatedDebugTrace()")
  public Object weaveJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    String className = methodSignature.getDeclaringType().getSimpleName();
    String methodName = methodSignature.getName();

    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed();

    LogUtil.e(buildLogMessage(className, methodName, System.currentTimeMillis() - startTime));

    return result;
  }

  /**
   * Create a log message.
   *
   * @param methodName A string with the method name.
   * @param methodDuration Duration of the method in milliseconds.
   * @return A string representing message.
   */
  private static String buildLogMessage(String className, String methodName, long methodDuration) {
    String message = className + " --> " + methodName + " --> " + "[" + methodDuration + "ms" + "]";

    return message;
  }
}
