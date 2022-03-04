package com.otto.catfish.pizza.order.clienthandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.io.OrderObject;

@Component
public class RestClientHandler {
	@Autowired
	private ApplicationContext ctx;

	private RestTemplate restTemplate;

	private String baseUrl;

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Bean
	public void getProperties() {
		baseUrl = ctx.getEnvironment().getProperty("pizza.order.status.url");
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

//	@HystrixCommand(fallbackMethod = "callGetOrderFallback", commandProperties = {
//			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
	public OrderObject callGetOrder(String orderId) throws OrderServiceException {

		restTemplate = new RestTemplate();
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("orderId", orderId);
		try {
			baseUrl = baseUrl + "/" + orderId;
			return this.restTemplate
					.exchange(baseUrl, HttpMethod.GET, new HttpEntity<>(getHeaders()), OrderObject.class).getBody();
		} catch (RestClientException e) {
			throw new OrderServiceException("Get order over :" + baseUrl, e);
		}
	}

	public int callGetStockCount(Long itemId) throws OrderServiceException {

		restTemplate = new RestTemplate();
		try {
			baseUrl = baseUrl + "/" + itemId;
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> body = this.restTemplate
					.exchange(baseUrl, HttpMethod.GET, new HttpEntity<>(getHeaders()), HashMap.class).getBody();
			return body.get("stock");
		} catch (RestClientException e) {
			throw new OrderServiceException("Get item count failed over :" + baseUrl, e);
		}
	}

}
