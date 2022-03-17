package com.otto.catfish.pizza.order.exception;

public class NotAllowedToCancelException extends Exception {

	private static final long serialVersionUID = 1L;

	public NotAllowedToCancelException(String msg) {
		super(msg);
	}
}
