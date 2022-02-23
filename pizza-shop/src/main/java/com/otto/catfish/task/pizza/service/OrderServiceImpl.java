package com.otto.catfish.task.pizza.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.otto.catfish.task.pizza.model.CustomerOrder;
import com.otto.catfish.task.pizza.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public CustomerOrder createOrder(CustomerOrder order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerOrder cancelOrder(Long orderId) {

		// TODO
		// check the order status
		// if not dispatched, then update the status as cancelled
		// notify restaurant
		// remove form queue
		// restore stock : increase the product quantity in catalog
		// return the updated order to user

		return null;
	}

	@Override
	public Page<CustomerOrder> findByCustomerEmail(String email, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerOrder getOrderByTrackingNumber(String trackingNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerOrder findByOrderId(Long orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<CustomerOrder> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
