package com.otto.catfish.pizza.order.io;

import java.util.List;

import com.otto.catfish.pizza.order.model.Item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class StockRecord implements KafkaRecord {

	private String orderId;
	private List<Item> items;
	private String operation;
}
