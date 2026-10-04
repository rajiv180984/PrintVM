package com.first.machin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
public class ControllerExceptionHandler /* extends ResponseEntityExceptionHandler */ {
	private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.error(ex.getMessage(), ex);
		return buildResponseEntity(new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiErrorResponse apiError) {
		return new ResponseEntity<>(apiError, HttpStatus.OK);
	}

	@ExceptionHandler(MachineExcepion.class)
	public final ResponseEntity<ApiErrorResponse> printException(MachineExcepion ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				ex.getCode(), ex.getCode());
		return new ResponseEntity<ApiErrorResponse> (exceptionResponse, HttpStatus.BAD_REQUEST);
	} 
	
	@ExceptionHandler(NoRecordFoundException.class)
	public final ResponseEntity<ApiErrorResponse> noRecordFoundException(NoRecordFoundException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				String.valueOf(ex.getErrorCode()));
		return new ResponseEntity<ApiErrorResponse> (exceptionResponse, HttpStatus.BAD_REQUEST);
	} 
	
	

}