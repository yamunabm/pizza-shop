package com.otto.catfish.pizza.stock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.ResponseEntity;

import com.otto.catfish.pizza.stock.controller.StockController;
import com.otto.catfish.pizza.stock.service.StockService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StockServiceApplicationTest {

	@InjectMocks
	private StockController stockController;

	@Mock
	private StockService stockService;

	// @Test
	public void contextLoads() throws Exception {
		assertThat(stockController).isNotNull();
	}

	// @Test
	public void getStocksCount_Success() {

		when(stockService.getStocksCount(anyLong())).thenReturn(2);

		ResponseEntity<Map<String, Integer>> stocksCount = stockController.getStocksCount(1L);

		Map<String, Integer> body = stocksCount.getBody();

		assertThat(body.get("stock")).isEqualTo(2);
	}

}
