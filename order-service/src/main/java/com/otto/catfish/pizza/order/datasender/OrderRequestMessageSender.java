package com.otto.catfish.pizza.order.datasender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.otto.catfish.pizza.order.io.KafkaRecord;
import com.otto.catfish.pizza.order.kafka.OrderKafkaProducer;
import com.otto.catfish.pizza.order.kafka.ProducerCallbackListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OrderRequestMessageSender implements ProducerCallbackListener, DataSender {

	@Value("${kafka.producer.order.request.topic}")
	private String orderRequestTopic;

	@Autowired
	private OrderKafkaProducer mProducer;

	@Override
	public void sendData(KafkaRecord data) {
		log.debug("Posting order request on Kafka");
		mProducer.sendData(data, this.orderRequestTopic, this);
	}

	@Override
	public void onDataPostedSuccessfully(KafkaRecord data) {
		log.info("Order request posted successfully on Kafka");
	}

	@Override
	public void onDataPostingFailed(KafkaRecord data) {
        log.warn("Failed to post Order request on Kafka");
	}
}