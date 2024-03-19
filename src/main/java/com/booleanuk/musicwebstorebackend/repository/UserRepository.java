package com.booleanuk.musicwebstorebackend.repository;

import com.booleanuk.musicwebstorebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
