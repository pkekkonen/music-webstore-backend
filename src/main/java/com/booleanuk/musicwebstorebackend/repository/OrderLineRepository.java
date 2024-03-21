package com.booleanuk.musicwebstorebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booleanuk.musicwebstorebackend.model.OrderLine;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine,Integer>{
    List<OrderLine> findByOrderId(int orderId);
    
}
