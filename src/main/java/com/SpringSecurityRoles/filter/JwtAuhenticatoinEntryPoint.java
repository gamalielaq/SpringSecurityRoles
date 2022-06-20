package com.SpringSecurityRoles.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.SpringSecurityRoles.constant.SecurityConstant;
import com.SpringSecurityRoles.domain.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtAuhenticatoinEntryPoint extends Http403ForbiddenEntryPoint {
    
    @Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		
        HttpResponse httpResponse = new HttpResponse(
            HttpStatus.FORBIDDEN.value(),  HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.getReasonPhrase().toUpperCase(), SecurityConstant.FORBIDDEN_MESSAGE
        );
        
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        OutputStream outputStream = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(outputStream, httpResponse);
        outputStream.flush();
	}
}
