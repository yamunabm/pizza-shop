package com.otto.catfish.pizza.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.otto.catfish.pizza.order.clienthandler.RestClientHandler;
import com.otto.catfish.pizza.order.common.Constants;
import com.otto.catfish.pizza.order.common.OrderEventType;
import com.otto.catfish.pizza.order.datasender.OrderRequestMessageSender;
import com.otto.catfish.pizza.order.datasender.StockUpdateMessageSender;
import com.otto.catfish.pizza.order.exception.NotAllowedToCancelException;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.exception.OutOfStockException;
import com.otto.catfish.pizza.order.exception.PaymentFailedException;
import com.otto.catfish.pizza.order.io.AddressVO;
import com.otto.catfish.pizza.order.io.ItemVO;
import com.otto.catfish.pizza.order.io.OrderObject;
import com.otto.catfish.pizza.order.io.OrderRequest;
import com.otto.catfish.pizza.order.io.OrderResponse;
import com.otto.catfish.pizza.order.io.OrderWrapper;
import com.otto.catfish.pizza.order.io.StockRecord;
import com.otto.catfish.pizza.order.model.Address;
import com.otto.catfish.pizza.order.model.Item;
import com.otto.catfish.pizza.order.model.Payment;
import com.otto.catfish.pizza.order.repository.AddressRepository;
import com.otto.catfish.pizza.order.repository.PaymentRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private StockUpdateMessageSender stockMessageSender;

	@Autowired
	private OrderRequestMessageSender orderkafkaMessageSender;

	private RestClientHandler restClientHandler;

	@Value("${order.check.status.url}")
	private String orderStatusUrl;

	@Override
	public OrderResponse createOrder(OrderRequest orderRequest)
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

		// Kafka: push order request to kafka
		OrderWrapper orderWarapper = new OrderWrapper(OrderEventType.NEW, orderRequest);
		orderkafkaMessageSender.sendData(orderWarapper);

		// return order Id with status code and location
		return new OrderResponse(orderId, orderStatusUrl + orderId);
	}

	private void lockOrder(OrderRequest orderRequest, String orderId)
			throws OrderServiceException, OutOfStockException {

		restClientHandler = new RestClientHandler();
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
	public OrderResponse cancelOrder(String orderId) throws NotAllowedToCancelException, OrderServiceException {

		// get Order status from notification service TODO

		restClientHandler = new RestClientHandler();
		restClientHandler.setBaseUrl("http://localhost:8090/pizza/v1/orderfulfillment");
		OrderObject callGetOrder = restClientHandler.callGetOrder(orderId);

		if (!OrderEventType.isAllwedToCancel(callGetOrder.getOrderStatus().getStatusId())) {
			throw new NotAllowedToCancelException("Product is not allaowed to cancel.");
		}

		updateOrderStatus(orderId, OrderEventType.CANCELLED);

		// TODO rollback payment

		// Restore stock
		// restoreStock(order);

		return new OrderResponse(orderId, orderStatusUrl + orderId);

	}

	private void updateOrderStatus(String orderId, OrderEventType cancelled) {
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(orderId);
		OrderWrapper orderWarapper = new OrderWrapper(OrderEventType.CANCELLED, orderRequest);
		orderkafkaMessageSender.sendData(orderWarapper);
	}

//
//	@Override
//	public OrderResponse cancelOrder(Long orderId) throws NotAllowedToCancelException {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
