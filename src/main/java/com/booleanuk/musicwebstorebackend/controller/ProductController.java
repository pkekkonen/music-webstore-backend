package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.*;
import com.booleanuk.musicwebstorebackend.model.DTO.ProductDTO;
import com.booleanuk.musicwebstorebackend.repository.*;
import com.booleanuk.musicwebstorebackend.payload.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.payload.response.Response;
import com.booleanuk.musicwebstorebackend.payload.response.SuccessResponse;
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
    @Autowired
    private RecordCompanyRepository recordCompanyRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ProductsGenreRepository productsGenreRepository;

    @GetMapping
    public ResponseEntity<Response<List<ProductDTO>>> getAllProducts() {
        List<ProductDTO> allProducts = this.productRepository.findAll().stream().map(this::toDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(allProducts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getProduct(@PathVariable int id) {
        ProductDTO product = this.toDto(findProduct(id));

        if(product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(product));
    }

    @PostMapping
    public ResponseEntity<Response<?>> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = this.toProduct(productDTO);
        if(containsNull(product)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
        }

        Product createdProduct = productRepository.save(product);

        productsGenreRepository.saveAll(product.getProductGenre());

        Response<ProductDTO> response = new SuccessResponse<>(this.toDto(createdProduct));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteProduct(@PathVariable int id) {
        Product productToDelete = findProduct(id);

        if(productToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        if(!productToDelete.getOrderLines().isEmpty() || !productToDelete.getReviews().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Product cannot be deleted, it has reviews or orders attached to it."));
        }

        if(productToDelete.getProductGenre() != null && !productToDelete.getProductGenre().isEmpty()) {
            productsGenreRepository.deleteAll(productToDelete.getProductGenre());
        }

        productRepository.delete(productToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(this.toDto(productToDelete)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateProduct(@PathVariable int id, @RequestBody ProductDTO productDTO) {
        Product productToUpdate = findProduct(id);
        productDTO.setId(id);
        if(productToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        Product updatedProduct = this.toProduct(productDTO);

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
        //TODO: Fix this so you can update genres as well
//        if(updatedProduct.getProductGenre() != null && !updatedProduct.getProductGenre().contains(null)) {
//            productsGenreRepository.deleteAll(productToUpdate.getProductGenre());
//            productsGenreRepository.saveAll(updatedProduct.getProductGenre());
//            productToUpdate.setProductGenre(updatedProduct.getProductGenre());
//        }


        Product savedProduct = productRepository.save(productToUpdate);
        ProductDTO response = this.toDto(savedProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(response));

    }

    private Product findProduct(int id) {
        return productRepository.findById(id).orElse(null);
    }

    private boolean containsNull(Product product) {
        return product.getTitle() == null || product.getReleaseYear() == null || product.getPrice() == null || product.getArtist() == null || product.getRecordCompany() == null || product.getProductGenre() == null;
    }

    private ProductDTO toDto(Product product) {
        if(product == null) {
            return null;
        }



        int id = product.getId();
        String title = product.getTitle();
        String artist = product.getArtist().getName();
        String recordCompany = product.getRecordCompany().getName();
        String releaseYear = product.getReleaseYear();
        Double price = product.getPrice();
        List<String> genres = product
                .getProductGenre()
                .stream()
                .map(ProductsGenre::getGenre)
                .map(Genre::getName)
                .toList();


        return new ProductDTO(id, title, artist, recordCompany, releaseYear, price, genres);
    }

    private Product toProduct(ProductDTO productDTO) {

        if(productDTO == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDTO.getId());
        product.setTitle(productDTO.getTitle());
        product.setReleaseYear(productDTO.getReleaseYear());
        product.setPrice(productDTO.getPrice());

        RecordCompany recordCompany = recordCompanyRepository.findByName(productDTO.getRecordCompany());
        product.setRecordCompany(recordCompany);

        Artist artist = artistRepository.findByName(productDTO.getArtist());
        product.setArtist(artist);

        if(productDTO.getGenres() != null) {
            List<ProductsGenre> productsGenres = productDTO.getGenres().stream()
                    .map(genreName -> {
                        Genre genre = genreRepository.findByName(genreName);
                        if (genre == null) {
                            return null;
                        }

                        return new ProductsGenre(product, genre);
                    })
                    .toList();

            product.setProductGenre(productsGenres.contains(null) ? null : productsGenres); //set to null if contains invalid genre}
        }
            return product;
        }
}

