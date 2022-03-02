package com.otto.catfish.pizza.order.datasender;

import com.otto.catfish.pizza.order.io.KafkaRecord;

public interface DataSender {

    void sendData(KafkaRecord data);
}
