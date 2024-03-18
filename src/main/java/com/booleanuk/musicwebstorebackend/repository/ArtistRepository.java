package com.booleanuk.musicwebstorebackend.repository;

import com.booleanuk.musicwebstorebackend.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist findByName(String name);
}