package com.SpringSecurityRoles.service.implement;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.SpringSecurityRoles.constant.UserImplConstant;
import com.SpringSecurityRoles.domain.User;
import com.SpringSecurityRoles.domain.UserPrincipal;
import com.SpringSecurityRoles.enumeration.Role;
import com.SpringSecurityRoles.exception.domain.EmailExistException;
import com.SpringSecurityRoles.exception.domain.UserNameExistException;
import com.SpringSecurityRoles.exception.domain.UserNotFoundException;
import com.SpringSecurityRoles.repository.UserRepository;
import com.SpringSecurityRoles.service.IUserService;
import com.SpringSecurityRoles.service.LoginAttemptService;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImp implements IUserService, UserDetailsService {
    
    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    public UserServiceImp(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByUsername(username);

        if (user == null) {
            LOGGER.error(UserImplConstant.USER_NOT_FOUND_BY_USERNAME + username);
            throw new UsernameNotFoundException(UserImplConstant.USER_NOT_FOUND_BY_USERNAME + username);
        } else {
        	this.validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            this.userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(UserImplConstant.FOUND_USER_BY_USERNAME + username);
            return userPrincipal;
        }
    }

    private void validateLoginAttempt(User user){
    	if(user.isNotLocked()) {
    		if(this.loginAttemptService.hasExceedMaxAttempts(user.getUsername())) { // 5 intentos de inicio de sesión
    			user.setNotLocked(false);
    		} else {
    			user.setNotLocked(true);    			
    		}
    	} else {
    		this.loginAttemptService.evicuserFromLoginAttempCache(user.getUsername());
    	}
	}

	@Override
    public User register(String firstname, String lastname, String username, String email) throws UserNotFoundException, UserNameExistException, EmailExistException {
        this.validateNewUserNameAndEmail(StringUtils.EMPTY, username, email);

        User user = new User();
        user.setUserId(generatedUserId());
        String password = generatePassword();
        String encodePassword = encodePassword(password);

        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setUsername(username);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRol(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryImgUrl());

        userRepository.save(user);

        LOGGER.info(password);
        LOGGER.info("Nuevo usuario password: ", password);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    private User validateNewUserNameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UserNameExistException, EmailExistException {

        User userByNewUsername = findUserByUserName(newUsername);
        User userByNewEmail = findUserByEmail(newEmail);

        if (StringUtils.isNotBlank(currentUsername)) {
            User currentUser = findUserByUserName(currentUsername);
            if (currentUser == null) {
                throw new UserNotFoundException(UserImplConstant.USER_NOT_FOUND_BY_USERNAME + currentUser);
            }
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())) {
                throw new UserNameExistException(UserImplConstant.USER_NAME_ALREADY_EXISTS);
            }
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())) {
                throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXISTS);
            }
            return currentUser;
        }
        if (userByNewUsername != null) {
            throw new UserNameExistException(UserImplConstant.USER_NAME_ALREADY_EXISTS);
        }
        if (userByNewEmail != null) {
            throw new EmailExistException(UserImplConstant.EMAIL_ALREADY_EXISTS);
        }
        return null;
    }

    private String getTemporaryImgUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(UserImplConstant.DEFAULT_USER_IMAGE_PATH).toUriString();
    }

    private String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generatedUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

}
