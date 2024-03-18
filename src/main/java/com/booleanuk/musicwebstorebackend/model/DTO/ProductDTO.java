package com.booleanuk.musicwebstorebackend.model.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;

public class ProductDTO {
    private int id;

    private String title;

    private String artist;

    private String recordCompany;

    private String releaseYear;

    private Double price;

    private List<String> genres;

    public ProductDTO(int id, String title, String artist, String recordCompany, String releaseYear, Double price, List<String> genres) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.recordCompany = recordCompany;
        this.releaseYear = releaseYear;
        this.price = price;
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getRecordCompany() {
        return recordCompany;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public Double getPrice() {
        return price;
    }

    public List<String> getGenres() {
        return genres;
    }
}
