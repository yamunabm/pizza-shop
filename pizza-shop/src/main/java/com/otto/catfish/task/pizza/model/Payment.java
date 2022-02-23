package com.otto.catfish.task.pizza.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.otto.catfish.task.pizza.util.Constants;
import com.otto.catfish.task.pizza.util.PaymentType;
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
@Table(name = "payment")
public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR)
	@Column(name = "PAYMENT_ID")
	private long paymentId;

	@OneToOne(targetEntity = CustomerOrder.class)
	@JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID", nullable = false)
	private CustomerOrder customerOrder;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "PAYMENT_TYPE")
	private PaymentType paymentType;

	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private Card card;

	@Column(name = "PAYMENT_DATE")
	private LocalDateTime paymentDate;

	@Column(name = "TRANSACTION_ID")
	private long transactionId;
}
