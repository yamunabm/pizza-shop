package com.otto.catfish.task.pizza.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otto.catfish.task.pizza.model.CustomerOrder;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long>{

}
