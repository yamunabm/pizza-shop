package com.otto.catfish.pizza.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otto.catfish.pizza.order.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
