package com.otto.catfish.pizza.order.adapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.otto.catfish.pizza.order.clienthandler.RestClientHandler;
import com.otto.catfish.pizza.order.common.OrderEventType;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.io.AddressVO;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;
import com.otto.catfish.pizza.order.model.Address;
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

	public static OrderResponse convertModelToOrderResponse(Order order, Optional<Address> address, RestClientHandler restClientHandler) throws OrderServiceException {

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setId(order.getId());
		orderResponse.setPaymentId(order.getPaymentId());
		
		if(address.isPresent()) {
			
			Address deliverAddress = address.get();
			
			AddressVO deliveryAddressVO = new AddressVO();
			deliveryAddressVO.setAddressId(deliverAddress.getId());
			deliveryAddressVO.setAddress(deliverAddress.getAddress());
			deliveryAddressVO.setCity(deliverAddress.getCity());
			deliveryAddressVO.setZipCode(deliverAddress.getZipCode());
			
			orderResponse.setAddress(deliveryAddressVO);
		}
		orderResponse.setCustomerId(order.getCustomerId());
		orderResponse.setOrderId(order.getOrderId());
		orderResponse.setOrderTimestamp(order.getOrderTimestamp());
		orderResponse.setTotalPrice(order.getTotalPrice());
		orderResponse.setOrderStatus(order.getOrderStatus());
		List<OrderItem> orderItems = order.getItems();
		List<Item> items = new ArrayList<Item>();
		for (OrderItem orderItem : orderItems) {
			Item item = restClientHandler.callGetItem(orderItem.getItemId());
			items.add(item);
		}
		orderResponse.setItems(items);
		return orderResponse;

	}

}
