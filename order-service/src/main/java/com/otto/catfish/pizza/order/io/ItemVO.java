package com.otto.catfish.pizza.order.io;

import com.otto.catfish.pizza.order.common.PizzaCrustType;
import com.otto.catfish.pizza.order.common.PizzaSizeType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class ItemVO {
	private long itemId;
	private double unitPrice;
	private int quantity;
	private String discuontId;
	private String name;
	private PizzaCrustType pizzaCrustType;
	private PizzaSizeType pizzaSizeType;
	private int[] toppings;
}
