package com.otto.catfish.pizza.stock.kafka;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.otto.catfish.pizza.stock.common.JsonMapper;
import com.otto.catfish.pizza.stock.exception.StockException;
import com.otto.catfish.pizza.stock.io.StockKafkaRequest;
import com.otto.catfish.pizza.stock.service.StockService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StockKafkaConsumerService {

	@Autowired
	private StockService stockService;

	@Autowired
	private JsonMapper jsonMapper;

	@KafkaListener(topics = "${kafka.consumer.stock.update.topic}", groupId = "${kafka.consumer.stock.update.groupId}", containerFactory = "stockKafkaListenerContainerFactory")
	public void initialiseConsumer(List<String> message, Acknowledgment acknowledgment) throws JsonProcessingException {
		
		try {
			if (null == message || message.isEmpty()) {
				log.info("Consumed stock message is empty!!");
				return;
			}
			log.debug("Consuming Items  :::: ");

			for (String msg : message) {
				StockKafkaRequest stock = jsonMapper.fromJson(msg, StockKafkaRequest.class);
				stockService.updateStocks(stock.getOrderId(),stock.getItems(), stock.getOperation());
			}
			acknowledgment.acknowledge();
		} catch (StockException e) {
			log.error("Failed while consuming Items !!", e.getMessage());
		}
	}
}