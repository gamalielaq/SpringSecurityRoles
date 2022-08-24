package com.SpringSecurityRoles.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class LoginAttemptService {
	private static final int MAXIMUN_NUMBER_OF_ATTEMPS = 5;
	private static final int ATTEMP_INCREMENT = 1;
	private LoadingCache<String, Integer> loginAttempCache;

	public LoginAttemptService() {
		super();
		loginAttempCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(100)
				.build(new CacheLoader<String, Integer>() {
					public Integer load(String key) {
						return 0;
					}
				});
	}
	
	public void evicuserFromLoginAttempCache(String username) {
		loginAttempCache.invalidate(username);;
	}
	
	public void addUserToLoginAttempCache(String username) {
		int attemts = 0;
		try {
			attemts = ATTEMP_INCREMENT + loginAttempCache.get(username);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		loginAttempCache.put(username, attemts);
	}
	
	public Boolean hasExceedMaxAttempts(String username) {
		try {
			return loginAttempCache.get(username) >= MAXIMUN_NUMBER_OF_ATTEMPS;
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return false;
	}
}
