package com.salesorder.microservice.salesorderservice.bo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OrderLineItem {
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "item_name")
	private String itemName;
	
	@Column(name = "item_quantity")
	private int itemQuantity;
	
	@Column(name = "order_id")
	private Long orderId;
	
	public OrderLineItem() {
		
	}

	public OrderLineItem(Long id, String itemName, int itemQuantity, Long orderId) {
		super();
		this.id = id;
		this.itemName = itemName;
		this.itemQuantity = itemQuantity;
		this.orderId = orderId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getItemQuantity() {
		return itemQuantity;
	}

	public void setItemQuantity(int itemQuantity) {
		this.itemQuantity = itemQuantity;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	
}
