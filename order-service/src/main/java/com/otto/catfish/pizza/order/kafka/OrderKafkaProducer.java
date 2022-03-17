package com.otto.catfish.pizza.order.kafka;

import java.util.Properties;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.otto.catfish.pizza.order.io.KafkaRecord;

import lombok.extern.slf4j.Slf4j;

/** Represents Food Donation Kafka Producer. */
@Slf4j
@Component
public class OrderKafkaProducer {
	private KafkaProducer<String, String> kafkaProducer;

	@Value("${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value("${kafka.config.retries}")
	private int kafkaRetries;

	private ObjectMapper mObjectMapper;

	/***
	 * Prepares KafkaProducer
	 */
	@PostConstruct
	public void init() {
		mObjectMapper = new ObjectMapper();
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		properties.put(ProducerConfig.RETRIES_CONFIG, kafkaRetries);
		properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization" + ".StringSerializer");
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization" + ".StringSerializer");
		this.kafkaProducer = new KafkaProducer<>(properties);
		log.debug("Initialised Kafka producer {}", bootstrapAddress);
	}

	/***
	 * Posts data to Kafka server
	 */
	public Future<RecordMetadata> sendData(KafkaRecord data, String topic, ProducerCallbackListener listener) {
		Future<RecordMetadata> future = null;
		try {
			future = kafkaProducer.send(new ProducerRecord<>(topic, mObjectMapper.writeValueAsString(data)),
					new ProducerCallBack(listener, data));
			log.debug("Sending data to kafka topic : {} ", topic);
		} catch (JsonProcessingException e) {
			log.error("sendData() ::: exception caught : {}", e.getMessage());
		}
		return future;
	}
}
