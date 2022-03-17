package com.otto.catfish.pizza.order.exception;

public class OrderNotFoundException extends Exception {
	private static final long serialVersionUID = 1L;

	public OrderNotFoundException(String msg) {
		super(msg);
	}
}
