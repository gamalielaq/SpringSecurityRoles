package com.SpringSecurityRoles.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import com.SpringSecurityRoles.domain.UserPrincipal;
import com.SpringSecurityRoles.service.LoginAttemptService;

// AuthenticationSuccessListener ete es un evento que spring dispara o emite automaticamente
@Component
public class AuthenticationSuccessListener {
	
	@Autowired
	private LoginAttemptService loginAttemptService;
	
	@EventListener
	public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
		Object principal = event.getAuthentication().getPrincipal();
		if(principal instanceof UserPrincipal) {
			UserPrincipal user = (UserPrincipal) event.getAuthentication().getPrincipal();
			this.loginAttemptService.evicuserFromLoginAttempCache(user.getUsername());
		}
	}
}
