package com.SpringSecurityRoles.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurityRoles.exception.domain.ExceptionHandlig;

@RestController
@RequestMapping("/user")
public class UserResource extends ExceptionHandlig {
    
    @GetMapping("/home")
    public String showUser() {
        return "Application works";
    }
}
