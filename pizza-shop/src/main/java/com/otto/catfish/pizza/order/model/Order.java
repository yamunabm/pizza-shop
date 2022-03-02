package com.otto.catfish.pizza.order.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.otto.catfish.pizza.order.common.Constants;
import com.otto.catfish.pizza.order.common.OrderEventType;
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
@Table(name = "OnlineOrder")
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR, strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "ORDER_ID")
	private String orderId;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Customer customer;

	private Long addressId;
	
	@OneToOne(targetEntity = Payment.class)
	private Payment payment;
	
	@Column(name = "TOTAL_PRICE")
	private double totalPrice;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private OrderEventType orderStatus;

	@Column(name = "TIMESTAMP")
	private LocalDateTime orderTimestamp;

	@OneToMany(targetEntity = OrderItem.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ORDER_ID", nullable = false)
	private List<OrderItem> items;

}
