package com.otto.catfish.pizza.stock.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.otto.catfish.pizza.stock.common.Constants;
import com.otto.catfish.pizza.stock.datasender.StockMessageSender;
import com.otto.catfish.pizza.stock.exception.OutOfStockException;
import com.otto.catfish.pizza.stock.exception.StockException;
import com.otto.catfish.pizza.stock.io.StockKafkaResponse;
import com.otto.catfish.pizza.stock.model.Item;
import com.otto.catfish.pizza.stock.repository.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StocksServiceImpl implements StockService {

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private StockMessageSender stockMessageSender;

	@Override
	public int getStocksCount(Long itemId) {
		return itemRepository.getStockCount(itemId);
	}

	@Override
	public void updateStocks(String orderId, List<Item> items, String opration) throws StockException {

		log.debug("Processing {} update operation for items count : {}", opration, items.size());
		StockKafkaResponse response = new StockKafkaResponse();

		try {
			List<Item> itemsToBeUpdated = new ArrayList<Item>();
			for (Item item : items) {
				Item itemToBeUpdated = itemRepository.findByItemId(item.getItemId());
				if (itemToBeUpdated != null) {

					if (opration.equals(Constants.RESTORE_STOCK))
						itemToBeUpdated.setStock(itemToBeUpdated.getStock() + item.getStock());
					if (opration.equals(Constants.DEDUCT_STOCK)) {
						if (itemToBeUpdated.getStock() < item.getStock())
							throw new OutOfStockException("Requested item is not available.");
						itemToBeUpdated.setStock(itemToBeUpdated.getStock() - item.getStock());
					}

					itemsToBeUpdated.add(itemToBeUpdated);
				}
			}
			itemRepository.saveAll(itemsToBeUpdated);
			response.setHttpResponseCode(HttpStatus.OK);
			log.debug("Successfully Processed {} update operation for items count : {}", opration, items.size());
		} catch (Exception e) {
			log.error("Error while processing {} update operation for items count : {} {}", opration, items.size(),
					e.getMessage());
			response.setHttpResponseCode(HttpStatus.BAD_REQUEST);
			response.setMessage("Failed due to :" + e.getMessage());
			response.setItemId(items);
			response.setOrderId(orderId);
			throw new StockException(e.getMessage());
		} finally {

			stockMessageSender.sendData(response);
		}

	}

	@Override
	public Item findByItemId(Long itemId) {
		return itemRepository.findByItemId(itemId);
	}

}
