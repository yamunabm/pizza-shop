package com.otto.catfish.task.pizza.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.otto.catfish.task.pizza.util.CardType;
import com.otto.catfish.task.pizza.util.Constants;
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
@Table(name = "card")
public class Card implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR)
	@Column(name = "CARD_ID")
	private long cardId;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "CARD_TYPE")
	private CardType cardType;

	@Column(name = "NAME_ON_CARD")
	private String nameOnCard;

	@Column(name = "CARD_NUMBER")
	private Long cardNumber;

	@Column(name = "EXPIRY_DATE")
	private LocalDate expiryDate;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false, insertable = false, updatable = false)
	private Customer customerId;
}
