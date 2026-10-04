package com.first.test.aop;

import java.lang.annotation.Annotation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.apache.bcel.classfile.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {
	private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

	// AOP expression for which methods shall be intercepted
	@Around("execution(* com.first.test.controller..*(..)))")
	public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		String className = methodSignature.getDeclaringType().getSimpleName();

		java.lang.reflect.Method method = methodSignature.getMethod();

		/*
		 * java.lang.reflect.Method method =
		 * proceedingJoinPoint.getTarget().getClass().getMethod(methodSignature.
		 * getMethod().getName(), methodSignature.getMethod().getParameterTypes());
		 */

		Annotation[] methodAnnotations = method.getDeclaredAnnotations();
		
		Annotation[] classAnnotations = proceedingJoinPoint.getTarget().getClass().getAnnotations();

		//System.out.println("======Class =========" + classAnnotations[1].toString());
		final StopWatch stopWatch = new StopWatch();

		// Measure method execution time
		stopWatch.start();
		Object result = proceedingJoinPoint.proceed();
		stopWatch.stop();

		// Log method execution time
		LOGGER.info("Execution time of " + className + "." + method + " :: " + stopWatch.getTotalTimeMillis() + " ms");

		return result;
	}
}