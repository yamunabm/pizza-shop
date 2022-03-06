package com.otto.catfish.pizza.order.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.exception.OutOfStockException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.CRUDOrderResponse;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;

public interface OrderService {

	public CRUDOrderResponse createOrder(OrderRequest order) throws PaymentFailedException, OrderServiceException, OutOfStockException;
	
	public CRUDOrderResponse cancelOrder(String orderId) throws NotAllowedToCancelException, OrderServiceException ;
	
	public List<OrderResponse> findAll(Pageable pageable) throws OrderServiceException;

	public OrderResponse findByOrderId(String orderId) throws OrderServiceException;

}
