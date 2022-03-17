package com.otto.catfish.pizza.stock.datasender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.otto.catfish.pizza.stock.io.StockKafkaResponse;
import com.otto.catfish.pizza.stock.kafka.ProducerCallbackListener;
import com.otto.catfish.pizza.stock.kafka.StockKafkaProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StockMessageSender implements ProducerCallbackListener, DataSender {

	@Value("${kafka.producer.stock.update.response.topic}")
	private String mKafkaTopic;

	@Autowired
	private StockKafkaProducer mProducer;

	@Override
	public void sendData(StockKafkaResponse response) {
		log.debug("Posting Stock update response on Kafka");
		mProducer.sendData(response, mKafkaTopic, this);
	}

	@Override
	public void onDataPostedSuccessfully(StockKafkaResponse response) {
		log.info("Stock update response posted successfully on Kafka");
	}

	@Override
	public void onDataPostingFailed(StockKafkaResponse response) {
        log.warn("Failed to post Stock update response on Kafka");
	}
}