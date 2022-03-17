package com.otto.catfish.pizza.stock.exception;

public class OutOfStockException extends Exception {

	private static final long serialVersionUID = 1L;

	public OutOfStockException(String msg) {
		super(msg);
	}

}
