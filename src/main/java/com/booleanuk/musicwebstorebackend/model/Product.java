package com.booleanuk.musicwebstorebackend.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.OffsetDateTime;

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

    //TODO: establish below connections to other tables when they are implemented

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "record_company_id", nullable = false)
//    @JsonIncludeProperties(value = {"id", "name"})
//    private RecordCompany recordCompany;
//
//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "artist_id", nullable = false)
//    @JsonIncludeProperties(value = {"name"})
//    private Artist artist;
//
//    @OneToMany(mappedBy = "product")
//    @JsonIgnoreProperties("product")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private List<ProductGenre> productGenres;
//
//    @OneToMany(mappedBy = "product")
//    @JsonIgnoreProperties("product")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private List<OrderLine> orderLines;
//
//    @OneToMany(mappedBy = "product")
//    @JsonIgnoreProperties("product")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private List<Review> reviews;

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

}