package com.otto.catfish.pizza.stock.io;

import java.util.List;

import com.otto.catfish.pizza.stock.model.Item;

import lombok.Data;

@Data
public class StockKafkaRequest {

	private String orderId;
	private List<Item> items;
	private String operation;
}
