package com.otto.catfish.pizza.order.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class OrderResponse {
	
	private String orderId;
	private String location;
}
