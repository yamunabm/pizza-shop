package com.otto.catfish.task.pizza.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.otto.catfish.pizza.order.common.PaymentType;
import com.otto.catfish.pizza.order.common.PizzaCrustType;
import com.otto.catfish.pizza.order.common.PizzaSizeType;
import com.otto.catfish.pizza.order.controller.OrderController;
import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.AddressVO;
import com.otto.catfish.pizza.order.io.ItemVO;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;
import com.otto.catfish.pizza.order.model.Payment;
import com.otto.catfish.pizza.order.repository.PaymentRepository;
import com.otto.catfish.pizza.order.service.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

	@InjectMocks
	private OrderController orderController;

	@Mock
	private OrderService orderService;

	@Mock
	private PaymentRepository paymentRepository;

	private ItemVO item1;
	private ItemVO item2;
	private AddressVO address;
	List<ItemVO> itemList = new ArrayList<ItemVO>();

	private OrderRequest orderRequest;

	@BeforeEach
	public void setUp() {

		int[] toppings = new int[] { 1, 3 };
		item1 = new ItemVO();
		item1.setItemId(1);
		item1.setName("pizza");
		item1.setQuantity(50);
		item1.setDiscuontId(null);
		item1.setUnitPrice(200);
		item1.setPizzaCrustType(PizzaCrustType.PAN);
		item1.setPizzaSizeType(PizzaSizeType.LARGE);
		item1.setToppings(toppings);

		item2 = new ItemVO();
		item2.setItemId(1);
		item2.setName("breadpizza");
		item2.setQuantity(100);
		item2.setDiscuontId(null);
		item2.setUnitPrice(100);
		item2.setPizzaCrustType(PizzaCrustType.THICK);
		item2.setPizzaSizeType(PizzaSizeType.SMALL);
		item2.setToppings(toppings);

		itemList.add(item1);
		itemList.add(item2);

		address = new AddressVO();
		address.setAddressId(1L);
		address.setAddress("#203, Royal road, Queen street");
		address.setCity("Bangalore");
		address.setZipCode("560048");

		Payment payment = new Payment();
		payment.setPaymentId(1);
		payment.setPaymentType(PaymentType.CASH);

		orderRequest = new OrderRequest();
		orderRequest.setItems(itemList);
		orderRequest.setAddress(address);
		orderRequest.setTotalPrice(300);
		orderRequest.setCustomerId(1L);
		orderRequest.setOrderId(UUID.randomUUID().toString());
		orderRequest.setPayment(payment);
	}

	@AfterEach
	public void tearDown() {

	}

	@Test
	public void createOrderWithValidRequest_Success() throws PaymentFailedException, NotAllowedToCancelException {
		ResponseEntity<OrderResponse> responseEntity = orderController.createOrder(orderRequest);
		assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
	}

	//@Test
	public void createOrderWithPaymentFailed_Failure() throws PaymentFailedException, NotAllowedToCancelException {

		Payment payment = new Payment();
		payment.setPaymentId(1L);
		payment.setPaymentType(PaymentType.CASH);
		when(paymentRepository.save(payment)).thenThrow(Exception.class);

		assertThrows(NotAllowedToCancelException.class, () -> {
			orderController.createOrder(orderRequest);
		});

	}

}
