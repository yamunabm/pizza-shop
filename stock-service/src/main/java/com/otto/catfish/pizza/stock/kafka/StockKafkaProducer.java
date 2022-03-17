package com.otto.catfish.pizza.stock.kafka;

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
import com.otto.catfish.pizza.stock.io.StockKafkaResponse;

import lombok.extern.slf4j.Slf4j;

/** Represents Stock Kafka Producer. */
@Slf4j
@Component
public class StockKafkaProducer {

	private KafkaProducer<String, String> kafkaProducer;

	@Value("${kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value("${kafka.config.retries}")
	private int kafkaRetries;

	private ObjectMapper bjectMapper;

	/***
	 * Prepares KafkaProducer
	 */
	@PostConstruct
	public void init() {
		bjectMapper = new ObjectMapper();
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
	public Future<RecordMetadata> sendData(StockKafkaResponse stockResponse, String topic,
			ProducerCallbackListener listener) {
		Future<RecordMetadata> future = null;
		try {
			future = kafkaProducer.send(new ProducerRecord<>(topic, bjectMapper.writeValueAsString(stockResponse)),
					new ProducerCallBack(listener, stockResponse));
			log.debug("Sending data to kafka topic : {} ", topic);
		} catch (JsonProcessingException e) {
			log.error("sendData() ::: exception caught : {}", e.getMessage());
		}
		return future;
	}
}
