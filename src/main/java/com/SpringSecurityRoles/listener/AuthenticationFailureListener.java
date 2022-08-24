package com.SpringSecurityRoles.listener;

import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import com.SpringSecurityRoles.service.LoginAttemptService;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

@Component
public class AuthenticationFailureListener {

	@Autowired
	private LoginAttemptService loginAttemptService;

	@EventListener
	public void onAuthenticationFailure(AuthenticationFailureBadCredentialsEvent event) throws ExecutionException {
		Object principal = event.getAuthentication().getPrincipal();
		if(principal instanceof String) {
			String username = (String) event.getAuthentication().getPrincipal();
			this.loginAttemptService.addUserToLoginAttempCache(username);
		}
	}
}