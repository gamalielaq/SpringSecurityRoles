package com.SpringSecurityRoles.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurityRoles.domain.User;
import com.SpringSecurityRoles.exception.domain.EmailExistException;
import com.SpringSecurityRoles.exception.domain.ExceptionHandlig;
import com.SpringSecurityRoles.exception.domain.UserNameExistException;
import com.SpringSecurityRoles.exception.domain.UserNotFoundException;
import com.SpringSecurityRoles.service.IUserService;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserResource extends ExceptionHandlig {

    @Autowired
    private IUserService userService;
    
    @GetMapping("/home")
    public String showUser() throws EmailExistException {
//        return "Application works";
    	throw new EmailExistException("Este correo electronico ya esta siendo usado OK");
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user ) throws UserNotFoundException, UserNameExistException, EmailExistException {
            User newUser = this.userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
            return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

}
