package com.otto.catfish.pizza.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.otto.catfish.pizza.order.adapter.BeanConversionAdapter;
import com.otto.catfish.pizza.order.clienthandler.RestClientHandler;
import com.otto.catfish.pizza.order.common.Constants;
import com.otto.catfish.pizza.order.common.OrderEventType;
import com.otto.catfish.pizza.order.datasender.StockUpdateMessageSender;
import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.exception.OutOfStockException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.AddressVO;
import com.otto.catfish.pizza.order.io.CRUDOrderResponse;
import com.otto.catfish.pizza.order.io.ItemVO;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;
import com.otto.catfish.pizza.order.io.StockRecord;
import com.otto.catfish.pizza.order.model.Address;
import com.otto.catfish.pizza.order.model.Item;
import com.otto.catfish.pizza.order.model.ItemToppings;
import com.otto.catfish.pizza.order.model.Order;
import com.otto.catfish.pizza.order.model.OrderItem;
import com.otto.catfish.pizza.order.model.Payment;
import com.otto.catfish.pizza.order.repository.AddressRepository;
import com.otto.catfish.pizza.order.repository.OrderRepository;
import com.otto.catfish.pizza.order.repository.PaymentRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private StockUpdateMessageSender stockMessageSender;

	@Autowired
	private RestClientHandler restClientHandler;

	@Value("${order.check.status.url}")
	private String orderStatusUrl;

	@Override
	public CRUDOrderResponse createOrder(OrderRequest orderRequest)
			throws PaymentFailedException, OrderServiceException, OutOfStockException {

		// TODO: generate order id and inject to request
		String orderId = UUID.randomUUID().toString();
		orderRequest.setOrderId(orderId);

		// Kafka: lock order : sync way
		lockOrder(orderRequest, orderId);

		// make payment
		Payment paymentId = makePayment(orderRequest);
		orderRequest.setPayment(paymentId);

		// save address
		if (orderRequest.getAddress().getAddressId() == null) {
			Address saveAddress = saveAddress(orderRequest);
			orderRequest.getAddress().setAddressId(saveAddress.getAddressId());
		}

		updateItemOrderToppingMapping(orderRequest);

		// return order Id with status code and location
		return new CRUDOrderResponse(orderId, orderStatusUrl + orderId);
	}

	private void lockOrder(OrderRequest orderRequest, String orderId)
			throws OrderServiceException, OutOfStockException {

		restClientHandler.setBaseUrl("http://localhost:9000/pizza/v1/item");

		List<Item> stocks = new ArrayList<Item>();
		for (ItemVO itemReq : orderRequest.getItems()) {

			int stockCount = restClientHandler.callGetStockCount(itemReq.getItemId());

			if (stockCount < itemReq.getQuantity())
				throw new OutOfStockException("Requested item " + itemReq.getItemId() + " count "
						+ itemReq.getQuantity() + "  is not available.");

			Item item = new Item();
			item.setItemId(itemReq.getItemId());
			item.setStock(itemReq.getQuantity());
			stocks.add(item);
		}
		stockMessageSender.sendData(new StockRecord(orderId, stocks, Constants.DEDUCT_STOCK));
	}

	private Order updateItemOrderToppingMapping(OrderRequest orderRequest) {
		List<OrderItem> orderItems = new ArrayList<OrderItem>(orderRequest.getItems().size());

		Order order = BeanConversionAdapter.convertOrderRequestToModel(orderRequest);

		for (ItemVO item : orderRequest.getItems()) {

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setItemId(item.getItemId());
			orderItem.setQuantity(item.getQuantity());
			int[] toppingArray = item.getToppings();
			List<ItemToppings> toppings = new ArrayList<ItemToppings>(toppingArray.length);
			for (int toppingId : toppingArray) {
				ItemToppings itemTopping = new ItemToppings();
				itemTopping.setOrderItem(orderItem);
				itemTopping.setToppingId(toppingId);
				toppings.add(itemTopping);
			}
			orderItem.setToppings(toppings);
			orderItems.add(orderItem);

		}
		order.setItems(orderItems);

		orderRepository.save(order);
		return order;

	}

	private Address saveAddress(OrderRequest orderRequest) {

		Address address = new Address();
		AddressVO addressVO = orderRequest.getAddress();
		address.setAddress(addressVO.getAddress());
		address.setCity(addressVO.getCity());
		address.setCustomerId(orderRequest.getCustomerId());
		address.setZipCode(addressVO.getZipCode());

		Address savedAddress = addressRepository.save(address);
		return savedAddress;

	}

	private Payment makePayment(OrderRequest orderRequest) throws PaymentFailedException {

		// sync call for payment. For now considering Payment is always successful
		try {
			Payment payment = paymentRepository.save(orderRequest.getPayment());
			return payment;
		} catch (Exception e) {
			throw new PaymentFailedException(e.getMessage());
		}
	}

	@Override
	public CRUDOrderResponse cancelOrder(String orderId) throws NotAllowedToCancelException, OrderServiceException {

		Order order = orderRepository.findByOrderId(orderId);

		if (!OrderEventType.isAllwedToCancel(order.getOrderStatus().getStatusId())) {
			throw new NotAllowedToCancelException("Product is not allaowed to cancel.");
		}

		// TODO rollback payment : payment-service

		List<OrderItem> items = order.getItems();
		// update order status
		updateOrderStatus(order);

		// Restore stock
		restoreStock(orderId, items);

		return new CRUDOrderResponse(orderId, orderStatusUrl + orderId);

	}

	private void updateOrderStatus(Order order) {
		order.setOrderStatus(OrderEventType.CANCELLED);
		orderRepository.save(order);
	}

	private void restoreStock(String orderId, List<OrderItem> items) {
		List<Item> stocks = new ArrayList<Item>();
		for (OrderItem itemReq : items) {
			Item item = new Item();
			item.setStock(itemReq.getQuantity());
			item.setItemId(itemReq.getItemId());
			stocks.add(item);
		}
		StockRecord stock = new StockRecord(orderId, stocks, Constants.RESTORE_STOCK);
		stockMessageSender.sendData(stock);
	}

	@Override
	public OrderResponse findByOrderId(String orderId) {
		Order order = orderRepository.findByOrderId(orderId);

		OrderResponse orderResponse = BeanConversionAdapter.convertModelToOrderResponse(order);

		return orderResponse;
	}

	@Override
	public List<OrderResponse> findAll(Pageable pageable) {
		Page<Order> orders = orderRepository.findAll(pageable);
		List<OrderResponse> orderResponseList = new ArrayList<OrderResponse>();

		for (Order order : orders) {
			OrderResponse orderResponse = BeanConversionAdapter.convertModelToOrderResponse(order);

			orderResponseList.add(orderResponse);
		}

		return orderResponseList;
	}

}
