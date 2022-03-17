package com.otto.catfish.pizza.order.exception;

public class OutOfStockException extends Exception {

	private static final long serialVersionUID = 1L;

	public OutOfStockException(String msg) {
		super(msg);
	}

}
