package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.Product;
import com.booleanuk.musicwebstorebackend.repository.ProductRepository;
import com.booleanuk.musicwebstorebackend.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.response.Response;
import com.booleanuk.musicwebstorebackend.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @GetMapping
    public ResponseEntity<Response<List<Product>>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(this.productRepository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getProduct(@PathVariable int id) {
        Product product = findProduct(id);

        if(product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(product));
    }

    @PostMapping
    public ResponseEntity<Response<?>> createProduct(@RequestBody Product product) {
        if(containsNull(product)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
        }

        Response<Product> response = new SuccessResponse<>(productRepository.save(product));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteProduct(@PathVariable int id) {
        Product productToDelete = findProduct(id);

        if(productToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        //TODO: what happens if it has order_lines, reviews or products_genres to it? delete those too? send error?

        productRepository.delete(productToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(productToDelete));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product productToUpdate = findProduct(id);
        if(productToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        if(product.getTitle() != null ) {
            productToUpdate.setTitle(product.getTitle());
        }
        if(product.getReleaseYear() != null ) {
            productToUpdate.setReleaseYear(product.getReleaseYear());
        }
        if(product.getPrice() != null ) {
            productToUpdate.setPrice(product.getPrice());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(productRepository.save(productToUpdate)));

    }

    private Product findProduct(int id) {
        return productRepository.findById(id).orElse(null);
    }

    private boolean containsNull(Product product) {
        return product.getTitle() == null || product.getReleaseYear() == null || product.getPrice() == null;
    }
}

