package com.booleanuk.musicwebstorebackend.repository;

import com.booleanuk.musicwebstorebackend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductIdAndUserId(int productId, int userId);
    Optional<Review> findByIdAndProductIdAndUserId(int id, int productId, int userId);
}
