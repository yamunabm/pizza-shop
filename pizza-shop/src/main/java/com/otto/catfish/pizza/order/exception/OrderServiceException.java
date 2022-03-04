package com.otto.catfish.pizza.order.exception;

import org.springframework.web.client.RestClientException;

public class OrderServiceException extends Exception {

	private static final long serialVersionUID = 1L;

	public OrderServiceException(String message) {
		super(message);
	}

	public OrderServiceException(String string, RestClientException e) {
		// TODO Auto-generated constructor stub
	}
}
