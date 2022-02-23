package com.otto.catfish.task.pizza.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.otto.catfish.task.pizza.model.CustomerOrder;

public interface OrderService {

	public CustomerOrder createOrder(CustomerOrder order);
	public CustomerOrder cancelOrder(Long orderId);
	public CustomerOrder getOrderByTrackingNumber(String trackingNumber);
    public CustomerOrder findByOrderId(Long orderId);
    public Page<CustomerOrder> findAll(Pageable pageable);
    public Page<CustomerOrder> findByCustomerEmail(String email, Pageable pageable);
}
