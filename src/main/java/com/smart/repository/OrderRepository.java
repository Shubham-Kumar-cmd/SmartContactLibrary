package com.smart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart.model.MyOrder;

public interface OrderRepository extends JpaRepository<MyOrder, Long> {
	
	MyOrder findByOrderId(String orderId);
}
