package com.otto.catfish.pizza.order.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.otto.catfish.pizza.order.common.Constants;
import com.otto.catfish.pizza.order.io.ErrorResponse;

@SuppressWarnings({ "unchecked", "rawtypes" })
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.name(), details);
		return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public final ResponseEntity<Object> handleBadRequestException(InvalidRequestException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.name(), details);
		return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NotAllowedToCancelException.class)
	public final ResponseEntity<Object> handleNotAllowedToCancelException(NotAllowedToCancelException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.name(), details);
		return new ResponseEntity(error, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(PaymentFailedException.class)
	public final ResponseEntity<Object> handlePaymentFailedException(PaymentFailedException ex, WebRequest request) {
		List<String> details = new ArrayList<>();
		details.add(ex.getLocalizedMessage());
		ErrorResponse error = new ErrorResponse(Constants.PAYMENT_FAILED, details);
		return new ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}