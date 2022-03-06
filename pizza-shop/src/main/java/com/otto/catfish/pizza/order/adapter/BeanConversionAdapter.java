package com.otto.catfish.pizza.order.adapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.otto.catfish.pizza.order.common.OrderEventType;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;
import com.otto.catfish.pizza.order.model.Item;
import com.otto.catfish.pizza.order.model.Order;
import com.otto.catfish.pizza.order.model.OrderItem;

public class BeanConversionAdapter {

	public static Order convertOrderRequestToModel(OrderRequest orderRequest) {
		Order order = new Order();

		order.setAddressId(orderRequest.getAddress().getAddressId());
		order.setCustomerId(orderRequest.getCustomerId());
		order.setOrderId(orderRequest.getOrderId());
		order.setPaymentId(orderRequest.getPayment().getPaymentId());
		order.setTotalPrice(orderRequest.getTotalPrice());
		order.setOrderStatus(OrderEventType.NEW);
		order.setOrderTimestamp(LocalDateTime.now());

		return order;
	}

	public static OrderResponse convertModelToOrderResponse(Order order) {

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setId(order.getId());
		orderResponse.setPaymentId(order.getPaymentId());
		orderResponse.setAddressId(order.getAddressId());
		orderResponse.setCustomerId(order.getCustomerId());
		orderResponse.setOrderId(order.getOrderId());
		orderResponse.setOrderTimestamp(order.getOrderTimestamp());
		orderResponse.setTotalPrice(order.getTotalPrice());
		orderResponse.setOrderStatus(order.getOrderStatus());
		List<OrderItem> orderItems = order.getItems();
		List<Item> items = new ArrayList<Item>();
		for (OrderItem orderItem : orderItems) {
			Item item = new Item();
			item.setItemId(orderItem.getItemId());
			items.add(item);
		}
		orderResponse.setItems(items);
		return orderResponse;

	}

}
