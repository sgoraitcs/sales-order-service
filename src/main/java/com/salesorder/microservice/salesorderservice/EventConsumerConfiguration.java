package com.salesorder.microservice.salesorderservice;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.BindingBuilder.GenericArgumentsConfigurer;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class EventConsumerConfiguration {

  @Bean
  public Exchange eventExchange() {
    return new TopicExchange("eventExchange");
  }

  @Bean
  public Queue queue() {
    return new Queue("customerServiceQueue");
  }

  @Bean
  public GenericArgumentsConfigurer binding(Queue queue, Exchange eventExchange) {
    return BindingBuilder
            .bind(queue)
            .to(eventExchange)
            .with("CustomerCreated");
  }

  @Bean
  public EventConsumer eventReceiver() {
    return new EventConsumer();
  }

}
