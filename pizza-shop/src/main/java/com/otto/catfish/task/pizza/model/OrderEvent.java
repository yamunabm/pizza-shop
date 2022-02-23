package com.otto.catfish.task.pizza.model;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import com.otto.catfish.task.pizza.util.Constants;
import com.otto.catfish.task.pizza.util.OrderEventType;
import com.sun.istack.NotNull;

@Entity
public class OrderEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR)
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