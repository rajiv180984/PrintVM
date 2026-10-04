package com.first.test.exception;

import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.TimeoutException;
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
import com.first.test.service.CustomerProfileService;
import com.first.test.util.ErrorCodeNo;
import com.first.test.util.GenericResponseEntity;
import com.first.test.util.ServiceStatus;
import com.first.test.util.ZeroxConstants;
import com.mongodb.MongoWriteException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RestController
public class ControllerExceptionHandler /* extends ResponseEntityExceptionHandler */ {
	private final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

	@Value("${zerox.bulkorder.pagethresold}")
	private int minPageCount;

	
	@Autowired
	CustomerProfileService customerProfileService;
	
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.error(ex.getMessage(), ex);
		return buildResponseEntity(new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiErrorResponse apiError) {
		return new ResponseEntity<>(apiError, HttpStatus.OK);
	}

	@ExceptionHandler(PrintRequestValidationException.class)
	public final ResponseEntity<GenericResponseEntity<ApiErrorResponse> > printException(PrintRequestValidationException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				ex.getErrorCode(), ErrorCodeNo.InsufficientParams.getValue());
		GenericResponseEntity<ApiErrorResponse> errorResp = new GenericResponseEntity<ApiErrorResponse>(exceptionResponse, ServiceStatus.FAILED.name(), ErrorCodeNo.InsufficientParams.getValue() );
		return new ResponseEntity<GenericResponseEntity<ApiErrorResponse> >(errorResp, HttpStatus.BAD_REQUEST);
	} 
	
	@ExceptionHandler(DataValidationException.class)
	public final ResponseEntity<GenericResponseEntity<ApiErrorResponse> > validationException(DataValidationException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				ErrorCodeNo.InsufficientParams.getValue(), ErrorCodeNo.InsufficientParams.getValue());
		GenericResponseEntity<ApiErrorResponse> errorResp = new GenericResponseEntity<ApiErrorResponse>(exceptionResponse, ServiceStatus.FAILED.name(), ErrorCodeNo.InsufficientParams.getValue() );
		return new ResponseEntity<GenericResponseEntity<ApiErrorResponse> >(errorResp, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> invalidInputException(MethodArgumentNotValidException ex,
			WebRequest request) {
		logger.error(ex.getMessage(), ex);
		String message = ex.getBindingResult().getFieldError().getDefaultMessage();

		ApiErrorResponse exceptionResponse = new ApiErrorResponse("Data Validation Failed: " + message,
				ErrorCodeNo.validationFailed.name(), ErrorCodeNo.validationFailed.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	// MongoWriteException

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> resourceNotFoundException(ResourceNotFoundException ex,
			WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				ZeroxConstants.RESOURCE_NOT_FOUND_KEY, ErrorCodeNo.NOTEXIST.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<GenericResponseEntity<ApiErrorResponse> >  AlreadyExistExceptionHandler(UserAlreadyExistException ex,
			WebRequest request) {
		logger.error(ex.getMessage(), ex);

		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage() ,
				String.valueOf(ex.getErrorCode()));
		exceptionResponse.setCode(String.valueOf(ex.getErrorCode()));
		GenericResponseEntity<ApiErrorResponse> errorResp = new GenericResponseEntity<ApiErrorResponse>(exceptionResponse, ServiceStatus.FAILED.name(), ex.getErrorCode() );
		return new ResponseEntity<GenericResponseEntity<ApiErrorResponse> >(errorResp, HttpStatus.ALREADY_REPORTED);

	}

	@ExceptionHandler(MongoWriteException.class)
	public ResponseEntity<ApiErrorResponse> MongoWriteExceptionExceptionHandler(RuntimeException ex,
			WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse("User Already Exist",
				ErrorCodeNo.DUPLICATE_DATA.name());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiErrorResponse> invalidAllRuntimeExceptionHandler(RuntimeException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(), ErrorCodeNo.UnableToProcess.name());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> invalidGenericExceptionHandler(Exception ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(), ErrorCodeNo.UnableToProcess.name());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(JsonMappingException.class)
	public ResponseEntity<ApiErrorResponse> invlaidJSONHandler(Exception ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		String message = ex.getMessage();
		logger.info("JsonMappingException : " + message);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse("Malformed JSON in request body",
				ErrorCodeNo.UnableToProcess.name());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoRecordFoundException.class)
	public ResponseEntity<ApiErrorResponse> norecordException(NoRecordFoundException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		String message = ex.getMessage();
		logger.info("JsonMappingException : " + message);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				String.valueOf(ErrorCodeNo.NoRecord.getValue()));
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(KafkaException.class)
	public final ResponseEntity<ApiErrorResponse> kafkaException(KafkaException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				ErrorCodeNo.InsufficientParams.getValue(), ErrorCodeNo.InsufficientParams.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(TimeoutException.class)
	public final ResponseEntity<ApiErrorResponse> TimeoutException(TimeoutException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				ErrorCodeNo.InsufficientParams.getValue(), ErrorCodeNo.InsufficientParams.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidDataException.class)
	public ResponseEntity<ApiErrorResponse> invalidInputException(InvalidDataException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		String message = ex.getMessage();
		logger.info("InvalidDataException : " + message);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse(ex.getMessage(),
				ErrorCodeNo.validationFailed.name(), ErrorCodeNo.validationFailed.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidFormatException.class)
	public ResponseEntity<ApiErrorResponse> invalidFormatException(InvalidFormatException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		String message = ex.getMessage();
		logger.info("InvalidDataException : " + message);
		ApiErrorResponse exceptionResponse = new ApiErrorResponse("Data Validation Failed: " + message,
				ErrorCodeNo.validationFailed.name(), ErrorCodeNo.validationFailed.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(InvalidBulkPrintException.class)
	public ResponseEntity<ApiErrorResponse> invalidFormatException(InvalidBulkPrintException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		String message = ex.getMessage();
		
		ApiErrorResponse exceptionResponse = new ApiErrorResponse("Invalid Bulk Order. Number of Copy should be greater : " + minPageCount,
				ErrorCodeNo.validationFailed.name(), ErrorCodeNo.validationFailed.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ExceptionHandler(ProfileException.class)
	public ResponseEntity<ApiErrorResponse> invalidFormatException(ProfileException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		String message = ex.getMessage();
		
		ApiErrorResponse exceptionResponse = new ApiErrorResponse("Failed to create profile",
				ex.getErrorCode(), ErrorCodeNo.ProfileFailed.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(PaymentFailureException.class)
	public ResponseEntity<ApiErrorResponse> paymentFailureExceptionHandle(PaymentFailureException ex, WebRequest request) {
		logger.error(ex.getMessage(), ex);
		String message = ex.getMessage();
		
		ApiErrorResponse exceptionResponse = new ApiErrorResponse("Failed to create profile",
				ex.getErrorCode(), ErrorCodeNo.AMOUNTLOCK.getValue());
		return new ResponseEntity<ApiErrorResponse>(exceptionResponse, HttpStatus.CONFLICT);
	}
	//
}