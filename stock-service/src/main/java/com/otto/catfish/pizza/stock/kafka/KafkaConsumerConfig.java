package com.otto.catfish.pizza.stock.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import com.otto.catfish.pizza.stock.model.Item;

@Configuration
public class KafkaConsumerConfig {

	@Value("${kafka.bootstrapAddress}")
	private String bootStrapServer;

	@Value("${kafka.consumer.stock.update.groupId}")
	private String groupId;

	@Value("${kafka.consumer.maxPollRecords}")
	private String maxPollRecords;

	public ConsumerFactory<String, List<Item>> stockConsumerFactory() {

		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);

		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, List<Item>> stockKafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, List<Item>> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(stockConsumerFactory());
		factory.setBatchListener(true);
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;
	}
}
