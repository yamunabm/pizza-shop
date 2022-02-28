package com.otto.catfish.pizza.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;

@Service
public class OrderServiceImpl implements OrderService {

	@Override
	public OrderResponse createOrder(OrderRequest order) throws PaymentFailedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderResponse> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponse findByOrderId(Long orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OrderResponse cancelOrder(Long orderId) throws NotAllowedToCancelException {
		// TODO Auto-generated method stub
		return null;
	}}
