package com.booleanuk.musicwebstorebackend.repository;


import com.booleanuk.musicwebstorebackend.model.RecordCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordCompanyRepository extends JpaRepository<RecordCompany, Integer> {
    RecordCompany findByName(String name);
}