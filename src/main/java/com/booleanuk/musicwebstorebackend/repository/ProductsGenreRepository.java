package com.booleanuk.musicwebstorebackend.repository;

import com.booleanuk.musicwebstorebackend.model.Product;
import com.booleanuk.musicwebstorebackend.model.ProductsGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsGenreRepository extends JpaRepository<ProductsGenre, Integer> {
    ProductsGenre findAllByProduct(Product product);
}