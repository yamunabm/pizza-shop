package com.otto.catfish.pizza.order.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;
import com.otto.catfish.pizza.order.service.OrderService;

@RestController
@RequestMapping(path = "/pizza/v1/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) throws PaymentFailedException {

		OrderResponse orders = orderService.createOrder(orderRequest);
		return ResponseEntity.ok(orders);  
	}
	
	@DeleteMapping("/{orderId}")
	public ResponseEntity<String> cancelOrder(@PathVariable("orderId") String orderId) throws NotAllowedToCancelException {

		orderService.cancelOrder(orderId);
		return ResponseEntity.ok("Success"); 
	}

}
