package com.bmworks;

import com.bmworks.rabbit.Receiver;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.http.client.Client;
import com.rabbitmq.http.client.domain.ExchangeInfo;
import com.rabbitmq.http.client.domain.QueueInfo;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Collections;

@SpringBootApplication
public class Main {

    @Bean
    RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        return new RabbitAdmin(rabbitTemplate);
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             Receiver receiver,
                                             RabbitAdmin rabbitAdmin) {

        rabbitAdmin.declareExchange(new TopicExchange("initialization", true, false));
        rabbitAdmin.declareQueue(new Queue("webstore", true, false, false));
        rabbitAdmin.declareBinding(new Binding("webstore", Binding.DestinationType.QUEUE, "initialization", "stock-management-up", Collections.emptyMap()));

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames("webstore");
        container.setMessageListener(receiver);
        return container;
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Main.class, args);
    }
}
