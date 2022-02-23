package com.otto.catfish.task.pizza.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "CustomerOrder")
public class CustomerOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR)
	@Column(name = "ORDER_ID")
	private long orderId;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOMER_ID", nullable = false, insertable = false, updatable = false)
	private Customer customerId;

	@OneToOne(optional = false, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private Address address;

	@OneToOne(mappedBy = "customerOrder", cascade = CascadeType.ALL)
	private Payment payment;

	@Column(name = "TOTAL_PRICE")
	private double totalPrice;

	@Column(name = "STATUS")
	private String orderStatus;

	@Column(unique = true, name = "TRACKING_NUMBER")
	private String trackingNumber;

	@Column(name = "TIMESTAMP")
	private LocalDateTime orderTimestamp;

	@OneToMany(targetEntity = OrderItem.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "ORDER_ID", nullable = false)
	private List<OrderItem> items;

}
