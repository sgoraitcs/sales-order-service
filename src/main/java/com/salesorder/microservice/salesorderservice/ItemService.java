package com.salesorder.microservice.salesorderservice;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.salesorder.microservice.salesorderservice.bo.SalesOrder;


@FeignClient(name ="item-service")
@RibbonClient(name ="item-service")
public interface ItemService {
	
	@GetMapping("/items/{itemname}")
	public SalesOrder findItem(@PathVariable("itemname") String itemName);

}
