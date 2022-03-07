package com.otto.catfish.task.pizza.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.otto.catfish.pizza.order.exception.OrderNotFoundException;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.exception.OutOfStockException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.AddressVO;
import com.otto.catfish.pizza.order.io.CRUDOrderResponse;
import com.otto.catfish.pizza.order.io.ItemVO;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.model.Address;
import com.otto.catfish.pizza.order.model.Order;
import com.otto.catfish.pizza.order.model.OrderItem;
import com.otto.catfish.pizza.order.model.Payment;
import com.otto.catfish.pizza.order.repository.AddressRepository;
import com.otto.catfish.pizza.order.repository.OrderRepository;
import com.otto.catfish.pizza.order.repository.PaymentRepository;
import com.otto.catfish.pizza.order.service.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

	@InjectMocks
	private OrderServiceImpl orderService;

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private OrderRepository orderRepository;

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

	private Order orderObject;

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
		orderObject = Order.builder().orderId("1234").id(1L).addressId(1L).orderStatus(OrderEventType.DISPATCHED)
				.items(orderItems).build();
	}

	@AfterEach
	public void tearDown() {
	}

	@Test
	public void createOrderWithValidRequest_Success()
			throws PaymentFailedException, OrderServiceException, OutOfStockException {
		when(restClientHandler.callGetStockCount(anyLong())).thenReturn(100);

		Payment payment = new Payment();
		payment.setPaymentId(1);
		payment.setPaymentType(PaymentType.CASH);

		when(paymentRepository.save(any())).thenReturn(payment);
		when(orderRepository.save(any())).thenReturn(new Order());

		CRUDOrderResponse createOrder = orderService.createOrder(orderRequest);
		assertNotNull(createOrder.getOrderId());

	}

	@Test
	public void createOrderWith_OutOfStockException()
			throws PaymentFailedException, OrderServiceException, OutOfStockException {
		when(restClientHandler.callGetStockCount(anyLong())).thenReturn(10);

		Address addressEntity = new Address();
		addressEntity.setId(1L);
		addressEntity.setAddress("#203, Royal road, Queen street");
		addressEntity.setCity("Bangalore");
		addressEntity.setZipCode("560048");

		Payment payment = new Payment();
		payment.setPaymentId(1);
		payment.setPaymentType(PaymentType.CASH);

		assertThrows(OutOfStockException.class, () -> {
			orderService.createOrder(orderRequest);
		});
	}

	@Test
	void cancelOrderWhenOrderIsNotYetDispatched() throws NotAllowedToCancelException, OrderServiceException, OrderNotFoundException {

		orderObject.setOrderStatus(OrderEventType.PENDING);
		when(orderRepository.findByOrderId(anyString())).thenReturn(orderObject);

		CRUDOrderResponse cancelOrder = orderService.cancelOrder("1234");

		assertTrue(cancelOrder.getOrderId().equals("1234"));
	}

	@Test
	void cancelOrderWhenOrderIsDispatched_throwNotAllowedToCancelException() throws OrderServiceException {
		when(orderRepository.findByOrderId(anyString())).thenReturn(orderObject);

		assertThrows(NotAllowedToCancelException.class, () -> {
			orderService.cancelOrder("1234");
		});
	}

}
