package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.exceptions.NetworkException;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;

@Aspect
@Order(1)
public class RetryAspect {

	private static final int MAX_RETRIES = 2;

	@Around("execution(* edu.sjsu.cmpe275.aop.ProfileService.*(..))")
	public Object aroundProfileServiceAdvice(ProceedingJoinPoint joinPoint) throws NetworkException {

		Object value = null;
		int retries = 0;

		for (; retries <= MAX_RETRIES; retries++) {
			try {
				value = joinPoint.proceed();
				return value;

			} catch (NetworkException e) {
				continue;

			} catch (Throwable t) {
				t.printStackTrace();
				break;
			}
		}

		if (retries > MAX_RETRIES)
			throw new NetworkException("No response from the server after two sucessful retries!");

		return value;
	}

}