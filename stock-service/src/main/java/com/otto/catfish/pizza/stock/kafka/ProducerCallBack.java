package com.otto.catfish.pizza.stock.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.otto.catfish.pizza.stock.io.StockKafkaResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProducerCallBack implements Callback {

	private final ProducerCallbackListener producerCallbackListener;
	private final StockKafkaResponse stockResponse;

	ProducerCallBack(ProducerCallbackListener producerCallbackListener, StockKafkaResponse stockResponse) {
		this.producerCallbackListener = producerCallbackListener;
		this.stockResponse = stockResponse;
	}

	@Override
	public void onCompletion(RecordMetadata recordMetadata, Exception e) {
		if (e == null) {
			producerCallbackListener.onDataPostedSuccessfully(stockResponse);
			log.debug("onCompletion() : data posted successfully {}"); 
		} else {
			log.error("onCompletion() : failed to send message failure exception is : {}", e.getMessage());
			producerCallbackListener.onDataPostingFailed(stockResponse);
		}
	}
}
