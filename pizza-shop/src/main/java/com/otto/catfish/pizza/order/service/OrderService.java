package com.otto.catfish.pizza.order.service;

import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.exception.OutOfStockException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;

public interface OrderService {

	public OrderResponse createOrder(OrderRequest order) throws PaymentFailedException, OrderServiceException, OutOfStockException;
	
	public OrderResponse cancelOrder(String orderId) throws NotAllowedToCancelException, OrderServiceException ;

}
