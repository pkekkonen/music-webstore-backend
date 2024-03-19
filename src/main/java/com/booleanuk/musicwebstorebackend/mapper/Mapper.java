package com.booleanuk.musicwebstorebackend.mapper;

import com.booleanuk.musicwebstorebackend.model.*;
import com.booleanuk.musicwebstorebackend.model.DTO.ProductDTO;
import com.booleanuk.musicwebstorebackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Mapper {
    @Autowired
    private RecordCompanyRepository recordCompanyRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ProductsGenreRepository productsGenreRepository;
    public ProductDTO toDto(Product product) {
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

    public Product toProduct(ProductDTO productDTO) {
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

        List<ProductsGenre> productsGenres = productDTO.getGenres().stream()
                .map(genreName -> {
                    Genre genre = genreRepository.findByName(genreName);
                    if(genre == null) {
                        return null;
                    }
                    return new ProductsGenre(product.getId(), genre.getId());
                })
                .toList();

        product.setProductGenre(productsGenres.contains(null)? null: productsGenres); //set to null if contains invalid genre

        return product;
    }
}