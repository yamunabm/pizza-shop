package com.otto.catfish.pizza.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.exception.OutOfStockException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.CRUDOrderResponse;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;
import com.otto.catfish.pizza.order.service.OrderService;

@RestController
@RequestMapping(path = "/pizza/v1/order")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<CRUDOrderResponse> createOrder(@RequestBody OrderRequest orderRequest)
			throws PaymentFailedException, OrderServiceException, OutOfStockException {

		CRUDOrderResponse orders = orderService.createOrder(orderRequest);
		return ResponseEntity.ok(orders);
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<CRUDOrderResponse> cancelOrder(@PathVariable("orderId") String orderId)
			throws NotAllowedToCancelException, OrderServiceException {

		CRUDOrderResponse cancelOrder = orderService.cancelOrder(orderId);
		return ResponseEntity.ok(cancelOrder);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderResponse> getOrderById(@PathVariable("orderId") String orderId) {

		OrderResponse order = orderService.findByOrderId(orderId);
		return ResponseEntity.ok(order);
	}

	@GetMapping
	public ResponseEntity<List<OrderResponse>> getOrders(Pageable pageable) {

		List<OrderResponse> order = orderService.findAll(pageable);
		return ResponseEntity.ok(order);
	}

}
