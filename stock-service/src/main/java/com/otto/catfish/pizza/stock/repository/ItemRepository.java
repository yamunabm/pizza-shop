package com.otto.catfish.pizza.stock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.otto.catfish.pizza.stock.common.Constants;
import com.otto.catfish.pizza.stock.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

	@Query(value = Constants.GET_STOCK_COUNT, nativeQuery = true)
	public int getStockCount(Long itemId);

	public Item findByItemId(Long itemId);

}
