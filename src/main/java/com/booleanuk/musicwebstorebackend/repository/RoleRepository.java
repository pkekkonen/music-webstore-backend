package com.booleanuk.musicwebstorebackend.repository;


import com.booleanuk.musicwebstorebackend.model.Role;
import com.booleanuk.musicwebstorebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByTitle(String title);

}