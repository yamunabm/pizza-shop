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
public class StockUpdateMessageSender implements ProducerCallbackListener, DataSender {

	@Value("${kafka.producer.stock.update.topic}")
	private String stockTopic;

	@Autowired
	private OrderKafkaProducer mProducer;

	@Override
	public void sendData(KafkaRecord data) {
		log.debug("Posting stock update request on Kafka");
		mProducer.sendData(data, this.stockTopic, this);
	}

	@Override
	public void onDataPostedSuccessfully(KafkaRecord data) {
		log.info("Stock update request data posted successfully on Kafka");
	}

	@Override
	public void onDataPostingFailed(KafkaRecord data) {
		log.warn("Failed to post Order Progress data on Kafka");
	}
}