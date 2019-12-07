package com.salesorder.microservice.salesorderservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.salesorder.microservice.salesorderservice.bo.SalesOrder;


@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>{

}
