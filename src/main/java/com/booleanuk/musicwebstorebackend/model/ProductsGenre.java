package com.booleanuk.musicwebstorebackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products_genres")
public class ProductsGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column
    private OffsetDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column
    private OffsetDateTime updatedAt;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIncludeProperties(value = {"id", "title", "release_year", "price"})
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    @JsonIncludeProperties(value = {"id", "name"})
    private Genre genre;



    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public ProductsGenre(int id) {
        this.id = id;
    }

    public ProductsGenre(int productId, int genreId) {
        this.product = new Product(productId);
        this.genre = new Genre(genreId);
    }

    public ProductsGenre(Product product, Genre genre) {
        this.product = product;
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "ProductsGenre{" +
                "id=" + id +
                ", product=" + product.getId() +
                ", genre=" + genre.getId() +
                '}';
    }
}
