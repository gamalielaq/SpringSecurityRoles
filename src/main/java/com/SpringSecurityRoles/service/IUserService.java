package com.SpringSecurityRoles.service;

import java.util.List;

import com.SpringSecurityRoles.domain.User;
import com.SpringSecurityRoles.exception.domain.EmailExistException;
import com.SpringSecurityRoles.exception.domain.UserNameExistException;
import com.SpringSecurityRoles.exception.domain.UserNotFoundException;

public interface IUserService {

    User register(String firstname, String lastname, String username, String email) throws UserNotFoundException, UserNameExistException, EmailExistException;

    List<User> getUsers();

    User findUserByUserName(String username);

    User findUserByEmail(String email);
}
