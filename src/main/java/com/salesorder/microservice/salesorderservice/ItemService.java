package com.salesorder.microservice.salesorderservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.salesorder.microservice.salesorderservice.bo.SalesOrder;

//@FeignClient(name ="item-service")
@FeignClient(name ="zuul-edge-server")
@RibbonClient(name ="item-service")
public interface ItemService {
	
	@GetMapping("/item-service/item/{itemname}")
	public SalesOrder findItem(@PathVariable("itemname") String itemname);

}
