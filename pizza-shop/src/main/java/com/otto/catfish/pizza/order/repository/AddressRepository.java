package com.otto.catfish.pizza.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otto.catfish.pizza.order.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
	
}

