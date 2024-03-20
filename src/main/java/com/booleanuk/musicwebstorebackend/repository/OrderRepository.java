package com.booleanuk.musicwebstorebackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booleanuk.musicwebstorebackend.model.Order;

public interface OrderRepository extends JpaRepository<Order,Integer>{
    
}
