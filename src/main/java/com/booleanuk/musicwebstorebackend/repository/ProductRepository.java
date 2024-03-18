package com.booleanuk.musicwebstorebackend.repository;


import com.booleanuk.musicwebstorebackend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}