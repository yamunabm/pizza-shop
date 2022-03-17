package com.otto.catfish.pizza.order.io;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdateResponse {
	
	private HttpStatus httpResponseCode;
	private String orderId;
	private String message;

}
