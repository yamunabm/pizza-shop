package com.otto.catfish.pizza.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.otto.catfish.pizza.order.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	public Customer getByCustomerId(Long customerId);
	
}
