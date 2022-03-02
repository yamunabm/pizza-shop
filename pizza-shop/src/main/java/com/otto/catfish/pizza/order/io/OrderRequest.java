package com.otto.catfish.pizza.order.io;

import java.util.List;

import com.otto.catfish.pizza.order.model.Payment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class OrderRequest {

	private Long customerId;
	private String orderId;
	private double totalPrice;
	private AddressVO address;
	private List<ItemVO> items;
	private Payment payment;
}
