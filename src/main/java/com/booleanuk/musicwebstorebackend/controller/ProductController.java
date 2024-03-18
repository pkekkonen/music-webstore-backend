package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.mapper.Mapper;
import com.booleanuk.musicwebstorebackend.model.DTO.ProductDTO;
import com.booleanuk.musicwebstorebackend.model.Product;
import com.booleanuk.musicwebstorebackend.model.ProductsGenre;
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

    private Mapper mapper;

    @GetMapping
    public ResponseEntity<Response<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> allProducts = this.productRepository.findAll().stream().map(mapper::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(allProducts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getProduct(@PathVariable int id) {
        ProductDTO product = mapper.toDto(findProduct(id));

        if(product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(product));
    }

    @PostMapping
    public ResponseEntity<Response<?>> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = mapper.toProduct(productDTO);
        if(containsNull(product)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
        }

        Response<ProductDTO> response = new SuccessResponse<>(mapper.toDto(productRepository.save(product)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteProduct(@PathVariable int id) {
        Product productToDelete = findProduct(id);

        if(productToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        //TODO: if it has order_lines, reviews or products_genres : SEND ERROR :)

        productRepository.delete(productToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(mapper.toDto(productToDelete)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {
        Product productToUpdate = findProduct(id);
        if(productToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        Product updatedProduct = mapper.toProduct(productDTO);

        if(updatedProduct.getTitle() != null ) {
            productToUpdate.setTitle(updatedProduct.getTitle());
        }
        if(updatedProduct.getReleaseYear() != null ) {
            productToUpdate.setReleaseYear(updatedProduct.getReleaseYear());
        }
        if(updatedProduct.getPrice() != null ) {
            productToUpdate.setPrice(updatedProduct.getPrice());
        }
        if(updatedProduct.getArtist() != null ) {
            productToUpdate.setArtist(updatedProduct.getArtist());
        }
        if(updatedProduct.getRecordCompany() != null ) {
            productToUpdate.setRecordCompany(updatedProduct.getRecordCompany());
        }
        if(!updatedProduct.getProductGenre().contains(null)) {
            productToUpdate.setRecordCompany(updatedProduct.getRecordCompany());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(mapper.toDto(productRepository.save(productToUpdate))));

    }

    private Product findProduct(int id) {
        return productRepository.findById(id).orElse(null);
    }

    private boolean containsNull(Product product) {
        return product.getTitle() == null || product.getReleaseYear() == null || product.getPrice() == null || product.getArtist() == null || product.getRecordCompany() == null || product.getProductGenre() == null;
    }
}

