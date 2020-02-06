package com.bmworks.rabbit;

import java.util.concurrent.CountDownLatch;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver implements MessageListener {

    private String stockManagementHost;

    @Override
    public void onMessage(Message message) {
        if (message.getMessageProperties().getReceivedRoutingKey().equals("stock-management-up")) {
            stockManagementHost = new String(message.getBody());
            System.out.println(message);
        }
    }

    public String getStockManagementHost() {
        return stockManagementHost;
    }
}
