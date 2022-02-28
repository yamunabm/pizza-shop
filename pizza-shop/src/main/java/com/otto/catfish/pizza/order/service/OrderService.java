package com.otto.catfish.pizza.order.service;

import java.util.List;

import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;

public interface OrderService {

	public OrderResponse createOrder(OrderRequest order) throws PaymentFailedException;
    public List<OrderResponse> findAll();
    public OrderResponse findByOrderId(Long orderId);
    public OrderResponse cancelOrder(Long orderId) throws NotAllowedToCancelException;
}
