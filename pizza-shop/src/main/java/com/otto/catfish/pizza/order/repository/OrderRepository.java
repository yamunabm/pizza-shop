package com.otto.catfish.pizza.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otto.catfish.pizza.order.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	public List<Order> findByCustomerEmailId(String emailId);

	public Order findByOrderId(Long orderId);

}
