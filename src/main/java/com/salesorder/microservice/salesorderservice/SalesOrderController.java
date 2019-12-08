package com.salesorder.microservice.salesorderservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.salesorder.microservice.salesorderservice.bo.ItemVO;
import com.salesorder.microservice.salesorderservice.bo.OrderLineItem;
import com.salesorder.microservice.salesorderservice.bo.OrderVO;
import com.salesorder.microservice.salesorderservice.bo.SalesOrder;


@RestController
public class SalesOrderController {
	
	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private OrderLineItemRepository orderLineItemRepository;
	
	@Autowired
	private ItemService itemService;
	
	@PostMapping("/orders")
	public Long createOrder(@RequestBody OrderVO  orderVO) throws ParseException{
		if(null == orderVO.getItems() || orderVO.getItems().isEmpty()) {
			return 0L;
		}
		//validate customer_sos
		long orderId = generateOrderId(salesOrderRepository.findAll());
		SalesOrder salesOrder;
		OrderLineItem orderLineItem;
		double price = 0;
		long lineItemId = generateLineItemId(orderLineItemRepository.findAll());
		for(ItemVO item : orderVO.getItems()) {
			salesOrder = itemService.findItem(item.getName());
			if(salesOrder.getId() > 0) {
				orderLineItem = new OrderLineItem(lineItemId, item.getName(), item.getQuantity(), orderId);
				orderLineItemRepository.save(orderLineItem);
				price+=salesOrder.getTotalPrice() * item.getQuantity();
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

}
