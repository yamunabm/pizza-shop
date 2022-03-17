package com.otto.catfish.pizza.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.otto.catfish.pizza.order.model.ItemToppings;

public interface ItemToppingRepository extends JpaRepository<ItemToppings, Long> {

}
