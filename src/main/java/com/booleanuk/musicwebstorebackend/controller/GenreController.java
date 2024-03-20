package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.Genre;
import com.booleanuk.musicwebstorebackend.payload.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.repository.GenreRepository;
import com.booleanuk.musicwebstorebackend.payload.response.Response;
import com.booleanuk.musicwebstorebackend.payload.response.SuccessResponse;
import com.booleanuk.musicwebstorebackend.payload.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.payload.response.Response;
import com.booleanuk.musicwebstorebackend.payload.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("genres")
public class GenreController {

    @Autowired
    private GenreRepository genreRepository;

    @GetMapping
    public ResponseEntity<Response<List<Genre>>> getAllGenres() {
        List<Genre> allGenres = this.genreRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(allGenres));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getAGenre(@PathVariable int id) {
        Genre genre = findGenre(id);
        if (genre == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Genre not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(genre));
    }

    @PostMapping
    public ResponseEntity<Response<?>> createGenre(@RequestBody Genre genre) {
        if (containsNull(genre)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Bad request"));
        }
        Response<Genre> res = new SuccessResponse<>(genreRepository.save(genre));

        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateGenre(@PathVariable int id, @RequestBody Genre genre) {
        Genre genreToUpdate = findGenre(id);
        if (genre == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Genre not found"));
        }
        if (genre.getName() != null) {
            genreToUpdate.setName(genre.getName());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(genreRepository.save(genreToUpdate)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteGenre(@PathVariable int id) {
        Genre genre = findGenre(id);
        if (genre != null) {
            if (genre.getProductGenres().isEmpty()) {
                genreRepository.deleteById(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse<>("Genre deleted"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Genre cannot be deleted"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Genre not found"));
        }
    }


    public Genre findGenre(int id) {
        return genreRepository.findById(id).orElse(null);
    }

    public boolean containsNull(Genre genre) {
        return genre.getName() == null;
    }
}
