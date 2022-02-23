package com.otto.catfish.task.pizza.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "orderitem")
public class OrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR)
	@Column(name = "ORDER_ITEM_ID")
	private Long orderItemId;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "ORDER_ID", nullable = false, insertable = false, updatable = false)
	private CustomerOrder customerOrder;

	@Column(name = "ITEM_ID")
	private Long itemId;

}
