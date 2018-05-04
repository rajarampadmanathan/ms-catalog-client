package com.example.demo.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class InvocationLogger {

	Logger log = LoggerFactory.getLogger(InvocationLogger.class);

	@Around(value = "execution( * com.example.demo..*.*(..))")
	public Object logBeforeInvocation(ProceedingJoinPoint jp) throws Throwable {
		long time = System.currentTimeMillis();
		Object obj = jp.proceed();
		log.info(String.format("Invoked Class:%s & Method: %s  $$$ Time Taken: %s", jp.getTarget().getClass().getName(),
				jp.getSignature().getName(), System.currentTimeMillis() - time));
		return obj;
	}
}
