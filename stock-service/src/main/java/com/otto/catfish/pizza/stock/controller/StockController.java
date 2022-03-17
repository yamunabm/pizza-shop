package com.otto.catfish.pizza.stock.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.otto.catfish.pizza.stock.model.Item;
import com.otto.catfish.pizza.stock.service.StockService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/pizza/v1/item")
public class StockController {

	@Autowired
	private StockService stockService;

	@GetMapping("/stock/{itemId}")
	public ResponseEntity<Map<String, Integer>> getStocksCount(@PathVariable("itemId") Long itemId) {

		log.debug("Received request for getStocksCount() : {}", itemId);
		int itemCount = stockService.getStocksCount(itemId);

		Map<String, Integer> entry = new HashMap<String, Integer>();

		entry.put("stock", itemCount);

		return ResponseEntity.ok(entry);

	}

	@GetMapping("/{itemId}")
	public ResponseEntity<Item> getItem(@PathVariable("itemId") Long itemId) {

		log.debug("Received request for getStocksCount() : {}", itemId);
		Item item = stockService.findByItemId(itemId);

		return ResponseEntity.ok(item);

	}
}
