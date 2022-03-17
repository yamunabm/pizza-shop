package com.otto.catfish.pizza.order.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "customer")
public class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR, strategy = GenerationType.IDENTITY)
	@Column(name = "CUSTOMER_ID")
	private long customerId;

	@Column(name = "FIRST_NAME")
	private String firstName;

	@Column(name = "LAST_NAME")
	private String lastName;

	@Column(name = "PHONE_NUMBER")
	private String phoneNumber;

	@Column(name = "EMAIL_ID")
	private String emailId;

//	@OneToMany(targetEntity = Address.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "CUSTOMER_ID", nullable = false)
//	private List<Address> addressList;
}
