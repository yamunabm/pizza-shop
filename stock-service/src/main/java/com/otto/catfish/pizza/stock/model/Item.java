package com.otto.catfish.pizza.stock.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.otto.catfish.pizza.stock.common.Constants;
import com.otto.catfish.pizza.stock.common.PizzaCrustType;
import com.otto.catfish.pizza.stock.common.PizzaSizeType;
import com.sun.istack.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "item")
public class Item implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR, strategy = GenerationType.IDENTITY)
	@Column(name = "ITEM_ID")
	private long itemId;

	@Column(name = "name")
	private String name;

	@Column(name = "UNIT_PRICE")
	private double unitPrice;

	@Column(name = "stock")
	private int stock;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "PIZZA_SIZE_TYPE")
	private PizzaSizeType pizzaSizeType;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "PIZZA_CRUST_TYPE")
	private PizzaCrustType pizzaCrustType;
}
