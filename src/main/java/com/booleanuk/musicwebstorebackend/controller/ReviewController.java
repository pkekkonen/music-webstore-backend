package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.Product;
import com.booleanuk.musicwebstorebackend.model.Review;
import com.booleanuk.musicwebstorebackend.model.User;
import com.booleanuk.musicwebstorebackend.payload.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.payload.response.Response;
import com.booleanuk.musicwebstorebackend.payload.response.SuccessResponse;
import com.booleanuk.musicwebstorebackend.repository.ProductRepository;
import com.booleanuk.musicwebstorebackend.repository.ReviewRepository;
import com.booleanuk.musicwebstorebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/{productId}/users/{userId}/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<Response<List<Review>>> getAllReviews(@PathVariable int productId, @PathVariable int userId) {
        List<Review> reviews = reviewRepository.findByProductIdAndUserId(productId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(reviews));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getAReview(@PathVariable int productId, @PathVariable int userId, @PathVariable int id) {
        Review review = reviewRepository.findByIdAndProductIdAndUserId(id, productId, userId).orElse(null);

        if (review == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Review not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(review));
    }

    @PostMapping
    public ResponseEntity<Response<?>> createReview(@PathVariable int productId, @PathVariable int userId, @RequestBody Review review) {
        if (containsNull(review)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Review content cannot be null"));
        }

        Product product = productRepository.findById(productId).orElse(null);
        User user = userRepository.findById(userId).orElse(null);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Product not found"));
        }

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("User not found"));
        }

        review.setProduct(product);
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(savedReview));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateReview(@PathVariable int productId, @PathVariable int userId, @PathVariable int id, @RequestBody Review review) {
        Review reviewToUpdate = reviewRepository.findByIdAndProductIdAndUserId(id, productId, userId).orElse(null);

        if (reviewToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Review not found"));
        }

        if (review.getContent() != null) {
            reviewToUpdate.setContent(review.getContent());
        }

        Review savedReview = reviewRepository.save(reviewToUpdate);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(savedReview));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteReview(@PathVariable int productId, @PathVariable int userId, @PathVariable int id) {
        Review reviewToDelete = reviewRepository.findByIdAndProductIdAndUserId(id, productId, userId).orElse(null);

        if (reviewToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Review not found"));
        }

        reviewRepository.delete(reviewToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(reviewToDelete));
    }

    public boolean containsNull(Review review) {
        return review.getContent() == null;
    }
}
