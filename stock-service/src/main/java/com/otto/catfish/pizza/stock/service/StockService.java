package com.otto.catfish.pizza.stock.service;

import java.util.List;

import com.otto.catfish.pizza.stock.exception.StockException;
import com.otto.catfish.pizza.stock.model.Item;

public interface StockService {

	public int getStocksCount(Long itemId);

	public void updateStocks(String orderId, List<Item> items, String opration) throws StockException;

	public Item findByItemId(Long itemId);
}
