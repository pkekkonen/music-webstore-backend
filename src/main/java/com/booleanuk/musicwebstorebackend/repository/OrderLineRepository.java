package com.booleanuk.musicwebstorebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booleanuk.musicwebstorebackend.model.OrderLine;

public interface OrderLineRepository extends JpaRepository<OrderLine,Integer>{
    
}
