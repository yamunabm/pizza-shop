package com.otto.catfish.pizza.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otto.catfish.pizza.order.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
