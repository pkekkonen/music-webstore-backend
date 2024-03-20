package com.booleanuk.musicwebstorebackend.repository;


import com.booleanuk.musicwebstorebackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByTitle(String title);
}