package com.otto.catfish.pizza.stock.kafka;

import com.otto.catfish.pizza.stock.io.StockKafkaResponse;

public interface ProducerCallbackListener {

  void onDataPostedSuccessfully(StockKafkaResponse stockResponse);

  void onDataPostingFailed(StockKafkaResponse stockResponse);
}
