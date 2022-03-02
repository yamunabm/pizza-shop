package com.otto.catfish.pizza.order.io;

import com.otto.catfish.pizza.order.common.OrderEventType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class OrderWrapper implements KafkaRecord{
	private OrderEventType status;
	private OrderRequest orderRequest;
}
