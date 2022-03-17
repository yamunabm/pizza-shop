package com.otto.catfish.pizza.order.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.otto.catfish.pizza.order.common.Constants;

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
@Table(name = "orderitem")
public class OrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR, strategy = GenerationType.IDENTITY)
	@Column(name = "ORDER_ITEM_ID")
	private Long orderItemId;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = false, insertable = false, updatable = false)
	private Order order;

	@Column(name = "ITEM_ID")
	private Long itemId;
	
	@Column(name = "QUANTITY")
	private int quantity;
	
	@OneToMany(targetEntity = ItemToppings.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ORDER_ITEM_ID", nullable = false)
	private List<ItemToppings> toppings;
}


