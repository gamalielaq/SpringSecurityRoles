package com.SpringSecurityRoles.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserResource {
    
    @GetMapping("/home")
    public String showUser() {
        return "Application works";
    }
}
