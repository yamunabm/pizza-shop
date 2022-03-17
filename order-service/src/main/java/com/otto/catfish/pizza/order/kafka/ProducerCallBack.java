package com.otto.catfish.pizza.order.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.otto.catfish.pizza.order.io.KafkaRecord;

import lombok.extern.slf4j.Slf4j;

/** Implementation of org.apache.kafka.clients.producer.Callback */
@Slf4j
public class ProducerCallBack implements Callback {

	private final ProducerCallbackListener mProducerCallbackListener;
	private final KafkaRecord kafkaRecord;

	ProducerCallBack(ProducerCallbackListener producerCallbackListener, KafkaRecord kafkaRecord) {
		mProducerCallbackListener = producerCallbackListener;
		this.kafkaRecord = kafkaRecord;
	}

	@Override
	public void onCompletion(RecordMetadata recordMetadata, Exception e) {
		if (e == null) {
			mProducerCallbackListener.onDataPostedSuccessfully(this.kafkaRecord);
		} else {
			log.error("onCompletion() ::: failed ::: failure exception is : {}", e.getMessage());
			mProducerCallbackListener.onDataPostingFailed(this.kafkaRecord);
		}
	}
}
