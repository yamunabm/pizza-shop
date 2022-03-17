package com.otto.catfish.pizza.stock.io;

import java.util.List;

import org.springframework.http.HttpStatus;

import com.otto.catfish.pizza.stock.model.Item;

import lombok.Data;

@Data
public class StockKafkaResponse {

	private String orderId;
	private HttpStatus httpResponseCode;
	private List<Item> itemId;
	private String message;

}
