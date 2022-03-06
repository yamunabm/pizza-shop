package com.otto.catfish.pizza.order.io;

import java.time.LocalDateTime;
import java.util.List;

import com.otto.catfish.pizza.order.common.OrderEventType;
import com.otto.catfish.pizza.order.model.Item;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
@Builder
public class OrderResponse {


	private Long id;
	
	private String orderId;
	
	private Long customerId;

	private AddressVO address;
	
	private Long paymentId;
	
	private double totalPrice;

	private OrderEventType orderStatus;

	private LocalDateTime orderTimestamp;

	private List<Item> items;


}
