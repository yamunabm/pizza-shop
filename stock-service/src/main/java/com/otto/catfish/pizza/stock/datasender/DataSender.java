package com.otto.catfish.pizza.stock.datasender;

import com.otto.catfish.pizza.stock.io.StockKafkaResponse;

public interface DataSender {

    void sendData(StockKafkaResponse response);
}
