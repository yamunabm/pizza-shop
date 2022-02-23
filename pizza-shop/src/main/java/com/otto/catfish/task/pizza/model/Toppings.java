package com.otto.catfish.task.pizza.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "Toppings")
public class Toppings implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = Constants.ID_GENERATOR)
	@Column(name = "TOPPING_ID")
	private long toppingId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PRICE")
	private String price;

}
