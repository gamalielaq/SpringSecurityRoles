package com.SpringSecurityRoles.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.SpringSecurityRoles.constant.SecurityConstant;
import com.SpringSecurityRoles.filter.JwtAccessDniedHandler;
import com.SpringSecurityRoles.filter.JwtAuhenticatoinEntryPoint;
import com.SpringSecurityRoles.filter.JwtAuthorizationFilter;


@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private JwtAuthorizationFilter jwtAuthorizationFilter;
    private JwtAccessDniedHandler jwtAccessDniedHandler;
    private JwtAuhenticatoinEntryPoint jwtAuhenticatoinEntryPoint;
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfiguration(
        JwtAuthorizationFilter jwtAuthorizationFilter,
        JwtAccessDniedHandler jwtAccessDniedHandler,
        JwtAuhenticatoinEntryPoint jwtAuhenticatoinEntryPoint,
        @Qualifier("userDetailsService")UserDetailsService userDetailsService,
        BCryptPasswordEncoder bCryptPasswordEncoder
    ) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtAccessDniedHandler = jwtAccessDniedHandler;
        this.jwtAuhenticatoinEntryPoint = jwtAuhenticatoinEntryPoint;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsService).passwordEncoder(this.bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeHttpRequests().antMatchers(SecurityConstant.PUBLIC_URLS).permitAll()
            .anyRequest().authenticated()
            .and().exceptionHandling().accessDeniedHandler(this.jwtAccessDniedHandler)
            .authenticationEntryPoint(this.jwtAuhenticatoinEntryPoint)
            .and().addFilterBefore(this.jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManagerBean();
    }    
}