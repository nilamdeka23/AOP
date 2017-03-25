package edu.sjsu.cmpe275.aop.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import edu.sjsu.cmpe275.aop.ProfileService;

import org.aspectj.lang.annotation.Aspect;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;

@Aspect
@Order(2)
public class AuthorizationAspect {

	@Autowired
	ProfileService profileService;

	/*
	 * Map for local persistence; Set to avoid duplicate references; HashSet and
	 * HasMap for their relative performance advantages
	 */
	private static Map<String, Set<String>> authMap = new HashMap<String, Set<String>>();

	/*
	 * Enforces the business rule: Once can share his profile with anybody; And
	 * If Alice shares her profile with Bob, Bob can further share Alice’s
	 * profile with Carl. If Alice attempts to share Bob’s profile with Carl
	 * while Bob’s profile is not shared with Alice in the first place, Alice
	 * gets an AccessDeniedExeption.
	 */
	@Before("execution(public void edu.sjsu.cmpe275.aop.ProfileService.shareProfile(..))")
	public void beforeShareAdvice(JoinPoint joinPoint) throws AccessDeniedException {

		Object[] args = joinPoint.getArgs();

		String userId = args[0].toString();
		String profileUserId = args[1].toString();

		if (!(profileUserId.equals(userId) || isAuthorized(profileUserId, userId))) {

			throw new AccessDeniedException("Insufficient Share Permission");
		}

	}

	/*
	 * Creates a record in local persistence for establishing future reference
	 */
	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.ProfileService.shareProfile(..))")
	public void afterShareAdvice(JoinPoint joinPoint) {

		Object[] args = joinPoint.getArgs();

		String profileUserId = args[1].toString();
		String targetUserId = args[2].toString();

		addToAuthMap(profileUserId, targetUserId);
	}

	/*
	 * Enforces the business rule: One can only read profiles that are shared
	 * with him, or his own profile. In any other case, an AccessDeniedExeption
	 * is thrown.
	 */
	@Before("execution(* edu.sjsu.cmpe275.aop.ProfileService.readProfile(..))")
	public void beforeReadAdvice(JoinPoint joinPoint) throws AccessDeniedException {

		Object[] args = joinPoint.getArgs();

		String userId = args[0].toString();
		String profileUserId = args[1].toString();

		if (!(profileUserId.equals(userId) || isAuthorized(profileUserId, userId))) {

			throw new AccessDeniedException("Insufficient Read Permission");
		}

	}

	/*
	 * Enforces the business rule: When unsharing a profile with Bob that the
	 * profile is not shared by any means with Bob in the first place, the
	 * operation throws an AccessDeniedExeption.
	 */
	@Before("execution(public void edu.sjsu.cmpe275.aop.ProfileService.unshareProfile(..))")
	public void beforeUnshareAdvice(JoinPoint joinPoint) throws AccessDeniedException {

		Object[] args = joinPoint.getArgs();

		String userId = args[0].toString();
		String targetUserId = args[1].toString();

		if (!(targetUserId.equals(userId) || isAuthorized(userId, targetUserId))) {

			throw new AccessDeniedException("Profile not shared originally");
		}

	}

	/*
	 * Updates local persistence
	 */
	@AfterReturning("execution(public void edu.sjsu.cmpe275.aop.ProfileService.unshareProfile(..))")
	public void afterUnshareAdvice(JoinPoint joinPoint) {

		Object[] args = joinPoint.getArgs();

		String userId = args[0].toString();
		String targetUserId = args[1].toString();

		removeFromAuthMap(userId, targetUserId);
	}

	/*
	 * Utility methods
	 */
	private boolean isAuthorized(String key, String value) {

		if (authMap.containsKey(key)) {
			Set<String> entityAuthList = authMap.get(key);

			if (entityAuthList.contains(value))
				return true;
		}

		return false;
	}

	private void addToAuthMap(String key, String value) {
		Set<String> entityAuthList;

		if (authMap.containsKey(key)) {
			entityAuthList = authMap.get(key);

		} else {
			entityAuthList = new HashSet<String>();
		}

		entityAuthList.add(value);
		authMap.put(key, entityAuthList);
	}

	private void removeFromAuthMap(String key, String value) {

		Set<String> entityAuthList = authMap.get(key);
		entityAuthList.remove(value);
	}

}
