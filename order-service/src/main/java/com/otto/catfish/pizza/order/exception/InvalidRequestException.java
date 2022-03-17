package com.otto.catfish.pizza.order.exception;

public class InvalidRequestException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

	public InvalidRequestException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}