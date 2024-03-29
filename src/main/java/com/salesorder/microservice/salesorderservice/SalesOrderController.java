package com.salesorder.microservice.salesorderservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.salesorder.microservice.salesorderservice.bo.CustomerSOS;
import com.salesorder.microservice.salesorderservice.bo.ItemVO;
import com.salesorder.microservice.salesorderservice.bo.OrderLineItem;
import com.salesorder.microservice.salesorderservice.bo.OrderVO;
import com.salesorder.microservice.salesorderservice.bo.SalesOrder;



@RestController
@EnableHystrix
public class SalesOrderController {
	
	private static Logger log = LoggerFactory.getLogger(SalesOrderController.class);
	
	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private CustomerSOSRepository customerSOSRepository;
	
	@Autowired
	private OrderLineItemRepository orderLineItemRepository;
	
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/item/{itemname}")
	public SalesOrder findItem(@PathVariable("itemname") String itemname) {
		SalesOrder item = itemService.findItem(itemname);
		return item;
	}
	
	@PostMapping("/orders")
	//@HystrixCommand(fallbackMethod="fallBackCreateOrders")
	public Long createOrders(@RequestBody OrderVO  orderVO) throws ParseException, SalesOrderException{
		log.info("Inside createOrder");	
		if(null == orderVO.getItems() || orderVO.getItems().isEmpty()) {
			return 0L;
		}
		CustomerSOS customer = customerSOSRepository.findOne(orderVO.getCustId());
		if(null == customer) {
			throw new SalesOrderException("Customer Not found");
		}
		//validate customer_sos
		long orderId = generateOrderId(salesOrderRepository.findAll());
		SalesOrder salesOrder;
		OrderLineItem orderLineItem;
		double price = 0;
		long lineItemId = generateLineItemId(orderLineItemRepository.findAll());
		String itemname;
		for(ItemVO item : orderVO.getItems()) {
			itemname = item.getName();
			salesOrder = itemService.findItem(itemname);
			if(null != salesOrder && salesOrder.getId() > 0) {
				orderLineItem = new OrderLineItem(lineItemId, item.getName(), item.getQuantity(), orderId);
				orderLineItemRepository.save(orderLineItem);
				price+=salesOrder.getTotalPrice() * item.getQuantity();
			} else {
				throw new SalesOrderException("Item Not found: "+itemname);
			}
			lineItemId++;
		}
		Date orderDate;
		if(null != orderVO.getOrderDate()) {
			SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yyyy");
			orderDate = fromUser.parse(orderVO.getOrderDate());
		} else {
			orderDate = new Date();
		}
		salesOrder = new SalesOrder(orderId, orderDate, orderVO.getCustId(), orderVO.getOrderDesc(), price);
		salesOrderRepository.save(salesOrder);
		return orderId;
		
	}
	
	public Long fallBackCreateOrders()  {
		return 0L;
	}
	
	private long generateLineItemId(List<OrderLineItem> list) {
		if(list == null) {
			return 1;
		}
		long id =0;
		for(OrderLineItem c : list) {
			if(id < c.getId()) {
				id=c.getId();
			}
		}
		return id+1;
	}
	
	private long generateOrderId(List<SalesOrder> list) {
		if(list == null) {
			return 1;
		}
		long id =0;
		for(SalesOrder c : list) {
			if(id < c.getId()) {
				id=c.getId();
			}
		}
		return id+1;
	}
	
	@PostMapping("/orders/fault-tolerance")
	@HystrixCommand(fallbackMethod="fallBackCreateOrders")
	public Long createOrdersFaultTolerance(@RequestBody OrderVO  orderVO) {
		log.info("Inside createOrdersFaultTolerance");	
		throw new RuntimeException("Order issue");
	}
	
	@PostMapping("/customer")
	public boolean createCustomerByEvent(@RequestBody CustomerSOS  customer){
		log.info("Inside createCustomerByEvent");		
		customerSOSRepository.save(customer);
		return true;
		
	}

}
