package com.SpringSecurityRoles.resource;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurityRoles.constant.SecurityConstant;
import com.SpringSecurityRoles.domain.User;
import com.SpringSecurityRoles.domain.UserPrincipal;
import com.SpringSecurityRoles.exception.domain.EmailExistException;
import com.SpringSecurityRoles.exception.domain.ExceptionHandlig;
import com.SpringSecurityRoles.exception.domain.UserNameExistException;
import com.SpringSecurityRoles.exception.domain.UserNotFoundException;
import com.SpringSecurityRoles.service.IUserService;
import com.SpringSecurityRoles.utility.JWTTokenProvider;

@RestController
@RequestMapping(path = { "/", "/user" })
public class UserResource extends ExceptionHandlig {

	@Autowired
    private IUserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        this.authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUserName(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws UserNotFoundException, UserNameExistException, EmailExistException, MessagingException {
        User newUser = this.userService.register(user.getFirstName(), user.getLastName(), user.getUsername(),user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    @GetMapping("/home")
    public String showUser() throws EmailExistException {
        throw new EmailExistException("Este correo electronico ya esta siendo usado OK");
    }
    
    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER , this.jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private  void authenticate(String username, String password) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

}
