package com.otto.catfish.pizza.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otto.catfish.pizza.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

	public Order findByOrderId(String orderId);

}
