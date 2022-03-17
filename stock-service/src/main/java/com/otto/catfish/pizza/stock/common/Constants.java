package com.otto.catfish.pizza.stock.common;

public class Constants {

	public static final String ID_GENERATOR = "ID_GENERATOR";
	public static final String GET_STOCK_COUNT = "SELECT stock FROM item where item_id = :itemId";
	public static final String DEDUCT_STOCK = "DEDUCT_STOCK";
	public static final String RESTORE_STOCK = "RESTORE_STOCK";
	public static final String OUT_OF_STOCK = "OUT_OF_STOCK";
}
