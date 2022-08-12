package com.SpringSecurityRoles.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurityRoles.exception.domain.EmailExistException;
import com.SpringSecurityRoles.exception.domain.ExceptionHandlig;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserResource extends ExceptionHandlig {
    
    @GetMapping("/home")
    public String showUser() throws EmailExistException {
//        return "Application works";
    	throw new EmailExistException("Este correo electronico ya esta siendo usado OK");
    }
}
