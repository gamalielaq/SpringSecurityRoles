package com.SpringSecurityRoles.exception.domain;

import java.io.IOException;
import java.util.Objects;

import javax.persistence.NoResultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.SpringSecurityRoles.domain.HttpResponse;
import com.auth0.jwt.exceptions.TokenExpiredException;


@RestControllerAdvice
public class ExceptionHandlig implements ErrorController{
	//Clase para manejar todas la excepciones
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final String ACCOUNT_LOCKED = "Su cuenta esta bloqueada. Porfavor contacte con el administrador";
	private static final String METHOD_IS_NOT_ALLOWED = "No esta permitido la solicitud al metodo para este end point. Porfavor envie un '%s' request";
	private static final String INTERNAL_SERVER_ERROR_MSG = "Ha ocurrido un error mientras se proceco su soliciud";
	private static final String INCORRECT_CREDENTIALS = "El usuario o contraseña es incorrecrta, Porfavor vuelva a intentar";
	private static final String ACCOUNT_DISABLED = "Su cuenta ha sido desabilitada, Si cree que es un error contacte con el administrador";
	private static final String ERROR_PROCESSING_FILE = "Ocurrio un error mientras se proceso el Archivo";
	private static final String NOT_ENOUGH_PERMISION = "Usted no tiene permisos suficientes";
	private static final String ERROR_PATH = "/error";
	
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> accountDisabledException() {
		return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<HttpResponse> badCredentialsException() {
		return this.createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<HttpResponse> accesDeniedException() {
		return this.createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISION);
	}
	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<HttpResponse> lockedException() {
		return this.createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED);
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException exception) {
		return this.createHttpResponse(HttpStatus.UNAUTHORIZED, exception.getMessage().toUpperCase());
	}
	
	@ExceptionHandler(EmailExistException.class)
	public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
		return this.createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
	
	@ExceptionHandler(EmailNotFoundException.class)
	public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException exception) {
		return this.createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
	
	@ExceptionHandler(UserNameExistException.class)
	public ResponseEntity<HttpResponse> userNameExistException(TokenExpiredException exception) {
		return this.createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<HttpResponse> userNameNotFoundException(UserNotFoundException exception) {
		return this.createHttpResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
	}
	
//	@ExceptionHandler(NoHandlerFoundException.class) //  metodo para controlar una url no mapeada
//	public ResponseEntity<HttpResponse> methodNotSupportedFoundException(NoHandlerFoundException e) {
//		return this.createHttpResponse(HttpStatus.BAD_REQUEST, "Esta pagina no fue encontrada");
//	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<HttpResponse> methodNotSupportedFoundException(HttpRequestMethodNotSupportedException exception) {
		HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods().iterator().next());
		return this.createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
		this.LOGGER.error(exception.getMessage());
		return this.createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
	}
	
	@ExceptionHandler(NoResultException.class)
	public ResponseEntity<HttpResponse> notFoundException(NoResultException exception) {
		LOGGER.error(exception.getMessage());
		return this.createHttpResponse(HttpStatus.NOT_FOUND, exception.getMessage());
	}
	
	@ExceptionHandler(IOException.class)
	public ResponseEntity<HttpResponse> ioException(IOException exception) {
		LOGGER.error(exception.getMessage());
		return this.createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
	}
	
	@RequestMapping(ERROR_PATH)
	public ResponseEntity<HttpResponse> notFound404() {
		return this.createHttpResponse(HttpStatus.NOT_FOUND, "La URL que busca no se encuetra en nuestra base");
	}
	
	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
		return new ResponseEntity<>(
				new HttpResponse(
						httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase()
						),
				httpStatus);
	}
}
