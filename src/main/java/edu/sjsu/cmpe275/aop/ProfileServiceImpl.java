package edu.sjsu.cmpe275.aop;

import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;

public class ProfileServiceImpl implements ProfileService{
	 
	/***
     * Following is a dummy implementation.
     * You can tweak the implementation to suit your need, but this file is NOT part of the submission.
     */
	
	@Override
	public Profile readProfile(String userId, String profileUserId) throws AccessDeniedExeption, NetworkException {
		// TODO Auto-generated method stub
		System.out.printf("User %s requests to read user %s's profile\n", userId, profileUserId);
		return null;
	}

	@Override
	public void shareProfile(String userId, String profileUserId, String targetUserId)
			throws AccessDeniedExeption, NetworkException {
		System.out.printf("User %s shares user %s's profile with user %s\n", userId, profileUserId, targetUserId);
		// TODO Auto-generated method stub
	}

	@Override
	public void unshareProfile(String userId, String targetUserId) throws AccessDeniedExeption, NetworkException {
		// TODO Auto-generated method stub
		System.out.printf("User %s unshares his/her own profile with user %s\n", userId, targetUserId);
	}

}
