package com.otto.catfish.pizza.order.kafka;

import com.otto.catfish.pizza.order.io.KafkaRecord;

/** Interface definition for org.apache.kafka.clients.producer.Callback */
public interface ProducerCallbackListener {

	void onDataPostedSuccessfully(KafkaRecord kafkaRecord);

	void onDataPostingFailed(KafkaRecord kafkaRecord);
}
