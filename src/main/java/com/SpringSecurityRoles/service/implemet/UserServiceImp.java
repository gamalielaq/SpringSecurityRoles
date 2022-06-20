package com.SpringSecurityRoles.service.implemet;

import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.SpringSecurityRoles.domain.User;
import com.SpringSecurityRoles.domain.UserPrincipal;
import com.SpringSecurityRoles.repository.UserRepository;
import com.SpringSecurityRoles.service.IUserService;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImp implements IUserService, UserDetailsService {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());

    private UserRepository userRepository;

    @Autowired
    public  UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = this.userRepository.findUserByUsername(username);

       if(user == null) {
        LOGGER.error("Usuario no encontrado con el username: " + username);
        throw new UsernameNotFoundException("Usuario no encontrado con el username: " + username);
       } else {
        user.setLastLoginDateDisplay(user.getLastLoginDate());
        user.setLastLoginDate(new Date());
        this.userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(user);
        LOGGER.info("Usuario encontrado con el username: "+username);
        return userPrincipal;
       }
    }

}
