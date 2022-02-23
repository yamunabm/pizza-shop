package com.otto.catfish.task.pizza.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.otto.catfish.task.pizza.util.Constants;

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
	@GeneratedValue(generator = Constants.ID_GENERATOR)
	@Column(name = "ITEM_ID")
	private long itemId;

	@Column(name = "UNIT_PRICE")
	private double unitPrice;
	
	@Column(name = "QUANTITY")
	private int quantity;
	
	@OneToMany(targetEntity = Toppings.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "TOPPING_ID", nullable = false, insertable = false, updatable = false)
	private List<Toppings> toppings;

	// private Category category;
}
