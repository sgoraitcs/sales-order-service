package com.salesorder.microservice.salesorderservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salesorder.microservice.salesorderservice.bo.CustomerSOS;

//@Component
public class EventConsumer {

	  private Logger logger = LoggerFactory.getLogger(EventConsumer.class);
	  
	  @Autowired
	  private CustomerSOSRepository customerSOSRepository;

	  @RabbitListener(queues="customerServiceQueue")
	  public void receive(CustomerSOS message) {
	    logger.info("Received message '{}'", message);
	    customerSOSRepository.save(message);
	  }

	}