package com.cesar.BookApi.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class Advice{

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		
		String parameter = ex.getName();
		Object value = ex.getValue();
		String required = ex.getRequiredType().getSimpleName();
		
		String message = String.format("'%s' debe ser un valor de tipo '%s' para '%s'", value, required, parameter);
		
		return ResponseEntity.badRequest().body( message );	
	}
	
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingRequestParameter(MissingServletRequestParameterException ex) {
		
		String parameter = ex.getParameterName();
		
		String message = String.format("Parameter '%s' is required. You can add many genres to filter. Example: '?genres=genre1, genre2'", parameter);
		
		return ResponseEntity.badRequest().body( message );	
	}
	
}
