package com.otto.catfish.pizza.order.model;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.otto.catfish.pizza.order.common.Constants;
import com.otto.catfish.pizza.order.common.OrderEventType;
import com.sun.istack.NotNull;

@Entity
public class OrderEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR, strategy = GenerationType.IDENTITY)
	@Column(name = "ORDER_EVENT_ID")
	private Long orderEventId;

	@NotNull
	@CreationTimestamp
	@Column(name = "OCCURED_ON")
	private Instant occurredOn;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "ORDER_EVENT_TYPE")
	private OrderEventType orderEventType;
}