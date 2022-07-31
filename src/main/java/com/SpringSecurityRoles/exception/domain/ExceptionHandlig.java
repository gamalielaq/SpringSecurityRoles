package com.SpringSecurityRoles.exception.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.SpringSecurityRoles.domain.HttpResponse;


@RestControllerAdvice
public class ExceptionHandlig {
	//Clase para manejar todas la excepciones
	private Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final String ACCOUNT_LOCKED = "Su cuenta esta bloqueada. Porfavor contacte con el administrador";
	private static final String METHOD_IS_NOT_ALLOWED = "No esta permitido la solicitud al metodo para este end point. Porfavor haga una nueva solicitud";
	private static final String INTERNAL_SERVER_ERROR_MSG = "A ocurrido un error miestrar se proceco su soliciud";
	private static final String INCORRECT_CREDENTIALS = "El usuario o contraseña es incorrecrta, Porfavor vuelva a intentar";
	private static final String ACCOUNT_DISABLED = "Su cuenta ha sido desabilitada, Si cree que es un error contacte con el administrador";
	private static final String ERROR_PROCESSING_FILE = "Ocurrio un error mientras se proceso el Archivo";
	private static final String NOT_ENOUGH_PERMISION = "Usted no tiene permisos suficientes";
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<HttpResponse> accountDisabledException() {
		return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
	}
	
	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message){
		return new ResponseEntity<>(
				new HttpResponse(
						httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase()
						),
				httpStatus);
	}
}
