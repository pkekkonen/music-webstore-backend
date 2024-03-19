package com.booleanuk.musicwebstorebackend.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String title;

    @Column
    private String releaseYear;

    @Column
    private Double price;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column
    private OffsetDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column
    private OffsetDateTime updatedAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "record_company_id", nullable = false)
    @JsonIncludeProperties(value = {"id", "name"})
    private RecordCompany recordCompany;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    @JsonIncludeProperties(value = {"name"})
    private Artist artist;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties("product")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ProductsGenre> productGenre;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties("product")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<OrderLine> orderLines;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties("product")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Review> reviews;

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

    public Product(String title, String releaseYear, double price) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.price = price;
    }

    public Product(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", price=" + price +
                ", recordCompany=" + recordCompany +
                ", artist=" + artist +
                ", productGenre=" + productGenre +
                ", orderLines=" + orderLines +
                ", reviews=" + reviews +
                '}';
    }
}