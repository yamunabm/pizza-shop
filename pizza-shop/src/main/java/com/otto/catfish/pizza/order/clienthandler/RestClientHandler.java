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
import org.springframework.web.util.UriComponentsBuilder;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.otto.catfish.pizza.order.exception.OrderServiceException;
import com.otto.catfish.pizza.order.io.OrderObject;

@Component
public class RestClientHandler {
	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private RestTemplate restTemplate;

	private static final String APIKEY = "APIkey";

	private String baseUrl;

	private String api;

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

	@HystrixCommand(fallbackMethod = "callGetOrderFallback", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
	public OrderObject callGetOrder(String orderId) throws OrderServiceException {
		Map<String, String> queryParams = new HashMap<>();
		queryParams.put("orderId", orderId);
		try {
			UriComponentsBuilder builder = getUriComponentsBuilder(baseUrl, queryParams);
			return this.restTemplate
					.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(getHeaders()), OrderObject.class)
					.getBody();
		} catch (RestClientException e) {
			throw new OrderServiceException("Get League failed over :" + baseUrl, e);
		}
	}

//	private OrderObject callGetOrderFallback(String orderId) {
//
//		System.out.println("CIRCUIT BREAKER ENABLED!!! Payment service is down!!! fallback route enabled...");
//
//		return null;
//	}

	private UriComponentsBuilder getUriComponentsBuilder(String url, Map<String, String> queryParams) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam(APIKEY, api);
		queryParams.forEach(builder::queryParam);
		return builder;
	}

	//	@HystrixCommand(fallbackMethod = "callGetOrderFallback", commandProperties = {
	//	@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000") })
	//public OrderObject callPayment(Payment payment) throws OrderServiceException {
	//Map<String, String> queryParams = new HashMap<>();
	//queryParams.put("payment", payment);
	//try {
	//	UriComponentsBuilder builder = getUriComponentsBuilder(baseUrl, queryParams);
	//	return this.restTemplate
	//			.exchange(builder.toUriString(), HttpMethod.GET, new HttpEntity<>(getHeaders()), OrderObject.class)
	//			.getBody();
	//} catch (RestClientException e) {
	//	throw new OrderServiceException("Get payment failed over :" + baseUrl, e);
	//}
	//}

}
