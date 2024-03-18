package com.booleanuk.musicwebstorebackend.repository;

import com.booleanuk.musicwebstorebackend.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Genre findByName(String name);
}