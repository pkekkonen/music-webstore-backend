package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.*;
import com.booleanuk.musicwebstorebackend.repository.*;
import com.booleanuk.musicwebstorebackend.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.response.Response;
import com.booleanuk.musicwebstorebackend.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("artists")
public class ArtistController {

    @Autowired
    private ArtistRepository artistRepository;

    @GetMapping
    public ResponseEntity<Response<List<Artist>>> getAllArtists() {
        List<Artist> allArtists = this.artistRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(allArtists));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getArtist(@PathVariable int id) {
        Artist artist = findArtist(id);

        if(artist == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(artist));
    }

    @PostMapping
    public ResponseEntity<Response<?>> createArtist(@RequestBody Artist artist) {
        if(containsNull(artist)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
        }

        Response<Artist> response = new SuccessResponse<>(artistRepository.save(artist));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteArtist(@PathVariable int id) {
        Artist artistToDelete = findArtist(id);

        if(artistToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        if(artistToDelete.getProducts() != null && !artistToDelete.getProducts().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Artist cannot be deleted, it has products attached to it."));
        }

        artistRepository.delete(artistToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(artistToDelete));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateArtist(@PathVariable int id, @RequestBody Artist artist) {
        Artist artistToUpdate = findArtist(id);
        if(artistToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }


        if(artist.getName() != null ) {
            artistToUpdate.setName(artist.getName());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(artistRepository.save(artistToUpdate)));
    }

    private Artist findArtist(int id) {
        return artistRepository.findById(id).orElse(null);
    }

    private boolean containsNull(Artist artist) {
        return artist.getName() == null;
    }
}
