package com.otto.catfish.pizza.order.io;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Data
public class AddressVO {
	private Long addressId;
	private String address;
	private String city;
	private String zipCode;
}
