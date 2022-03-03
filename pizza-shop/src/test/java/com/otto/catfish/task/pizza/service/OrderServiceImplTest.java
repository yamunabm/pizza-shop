package com.otto.catfish.task.pizza.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.otto.catfish.pizza.order.clienthandler.RestClientHandler;
import com.otto.catfish.pizza.order.common.OrderEventType;
import com.otto.catfish.pizza.order.common.PaymentType;
import com.otto.catfish.pizza.order.common.PizzaCrustType;
import com.otto.catfish.pizza.order.common.PizzaSizeType;
import com.otto.catfish.pizza.order.datasender.OrderRequestMessageSender;
import com.otto.catfish.pizza.order.datasender.StockUpdateMessageSender;
import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.AddressVO;
import com.otto.catfish.pizza.order.io.ItemVO;
import com.otto.catfish.pizza.order.io.OrderObject;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;
import com.otto.catfish.pizza.order.model.Address;
import com.otto.catfish.pizza.order.model.OrderItem;
import com.otto.catfish.pizza.order.model.Payment;
import com.otto.catfish.pizza.order.repository.AddressRepository;
import com.otto.catfish.pizza.order.repository.PaymentRepository;
import com.otto.catfish.pizza.order.service.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

	@InjectMocks
	private OrderServiceImpl orderService;

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private AddressRepository addressRepository;

	@Mock
	private StockUpdateMessageSender stockMessageSender;

	@Mock
	private OrderRequestMessageSender orderkafkaMessageSender;

	@Mock
	private RestClientHandler restClientHandler;

	private ItemVO item1;
	private ItemVO item2;
	private AddressVO address;
	List<ItemVO> itemList = new ArrayList<ItemVO>();

	private OrderRequest orderRequest;

	private OrderObject orderObject;

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
		orderRequest.setOrderId("12345");
		orderRequest.setPayment(payment);

		List<OrderItem> orderItems = new ArrayList<OrderItem>();

		orderItems.add(new OrderItem(1L, null, 1L, 10, Collections.emptyList()));
		orderObject = OrderObject.builder().orderId("1234").id(1L).addressId(1L).orderStatus(OrderEventType.DISPATCHED)
				.build();
	}

	@AfterEach
	public void tearDown() {
	}

	@Test
	public void createOrderWithValidRequest_Success() throws PaymentFailedException {
		doNothing().when(orderkafkaMessageSender).sendData(any());

		Address addressEntity = new Address();
		addressEntity.setAddressId(1L);
		addressEntity.setAddress("#203, Royal road, Queen street");
		addressEntity.setCity("Bangalore");
		addressEntity.setZipCode("560048");

		when(addressRepository.save(any())).thenReturn(addressEntity);

		OrderResponse createOrder = orderService.createOrder(orderRequest);
		verify(orderkafkaMessageSender).sendData(any());

		assertNotNull(createOrder.getOrderId());

	}

	@Test
	void cancelOrderWhenOrderIsNotYetDispatched() throws NotAllowedToCancelException, OrderServiceException {
		doNothing().when(orderkafkaMessageSender).sendData(any());
		orderObject.setOrderStatus(OrderEventType.PENDING);
		when(restClientHandler.callGetOrder(anyString())).thenReturn(orderObject);

		OrderResponse cancelOrder = orderService.cancelOrder("1234");

		verify(orderkafkaMessageSender).sendData(any());
		assertTrue(cancelOrder.getOrderId().equals("1234"));
	}

	@Test
	void cancelOrderWhenOrderIsDispatched_throwNotAllowedToCancelException() throws OrderServiceException {
		when(restClientHandler.callGetOrder(anyString())).thenReturn(orderObject);

		assertThrows(NotAllowedToCancelException.class, () -> {
			orderService.cancelOrder("1234");
		});
	}

	// success ---
	// when all the input fields are provided
	// when the toppings are specified
	// when customer has entered the address at the time of order
	// when no toppings specified
	// when address not entered from the customer, then use the default address
	// when the payment failed due to insufficient funds
	// when payment failed due to service is down

	// when user cancelled the order
	// when user tries to cancel the order but the item is dispatched, since unble
	// to cancel

}