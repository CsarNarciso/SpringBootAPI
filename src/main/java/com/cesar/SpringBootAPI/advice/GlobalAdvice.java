package com.cesar.SpringBootAPI.advice;

import com.cesar.SpringBootAPI.dto.ApplicationResponse;
import com.cesar.SpringBootAPI.exception.NoContentException;
import com.cesar.SpringBootAPI.exception.NotFoundException;
import com.cesar.SpringBootAPI.util.ApplicationResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

import java.util.Map;

@ControllerAdvice
public class GlobalAdvice{

	@ExceptionHandler(NoContentException.class)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ApplicationResponse<Map<String, Object>> handleNoContent() {
		return ApplicationResponseUtils.buildResponse(204, "No content found for this resource", null, null);
	}

	@ExceptionHandler({NotFoundException.class, NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApplicationResponse<Map<String, Object>> handleNotFound() {
		return ApplicationResponseUtils.buildResponse(404, "Resource not found", null, null);
	}


	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<?> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex){

		HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
		int httpCode = httpStatus.value();

		ApplicationResponse<?> response = new ApplicationResponse<>(
				httpCode,
				"Media type not supported. Only supported JSON values.",
				null);

		return ResponseEntity.status(httpStatus).body(response);
	}
	
	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleMessageNotReadable(HttpMessageNotReadableException ex){

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		int httpCode = httpStatus.value();

		ApplicationResponse<?> response = new ApplicationResponse<>(
				httpCode,
				"Not readable value in body.",
				null);

		return ResponseEntity.status(httpStatus).body(response);
	}
	

	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		int httpCode = httpStatus.value();

		String parameter = ex.getName();
		Object value = ex.getValue();
		String required = ex.getRequiredType().getSimpleName();

		String message = String.format("'%s' must be a type '%s' value for '%s'", value, required, parameter);

		ApplicationResponse<?> response = new ApplicationResponse<>(
				httpCode,
				message,
				null);

		return ResponseEntity.status(httpStatus).body(response);
	}
	
	
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingRequestParameter(MissingServletRequestParameterException ex) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		int httpCode = httpStatus.value();

		String parameter = ex.getParameterName();
		String message = String.format("Parameter '%s' is required.", parameter);

		ApplicationResponse<?> response = new ApplicationResponse<>(
				httpCode,
				message,
				null);

		return ResponseEntity.status(httpStatus).body(response);
	}
	
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		int httpCode = httpStatus.value();

		ApplicationResponse<?> response = new ApplicationResponse<>(
				httpCode,
				ex.getFieldError().getDefaultMessage(),
				null);

		return ResponseEntity.status(httpStatus).body(response);
	}
	
	
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {

		HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
		int httpCode = httpStatus.value();

		StringBuilder message = new StringBuilder();
		ex.getConstraintViolations().stream().forEach( v ->
				message.append( v.getMessage() + " " )
		);

		ApplicationResponse<?> response = new ApplicationResponse<>(
				httpCode,
				message.toString(),
				null);

		return ResponseEntity.status(httpStatus).body(response);
	}
	
}