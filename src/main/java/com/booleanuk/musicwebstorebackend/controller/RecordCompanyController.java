package com.booleanuk.musicwebstorebackend.controller;

import com.booleanuk.musicwebstorebackend.model.*;
import com.booleanuk.musicwebstorebackend.repository.*;
import com.booleanuk.musicwebstorebackend.payload.response.ErrorResponse;
import com.booleanuk.musicwebstorebackend.payload.response.Response;
import com.booleanuk.musicwebstorebackend.payload.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("record_companies")
public class RecordCompanyController {

    @Autowired
    private RecordCompanyRepository recordCompanyRepository;

    @GetMapping
    public ResponseEntity<Response<List<RecordCompany>>> getAllRecordCompanies() {
        List<RecordCompany> allRecordCompanies = this.recordCompanyRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(allRecordCompanies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getRecordCompany(@PathVariable int id) {
        RecordCompany recordCompany = findRecordCompany(id);

        if(recordCompany == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(recordCompany));
    }

    @PostMapping
    public ResponseEntity<Response<?>> createRecordCompany(@RequestBody RecordCompany recordCompany) {
        if(containsNull(recordCompany)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
        }

        Response<RecordCompany> response = new SuccessResponse<>(recordCompanyRepository.save(recordCompany));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteRecordCompany(@PathVariable int id) {
        RecordCompany recordCompanyToDelete = findRecordCompany(id);

        if(recordCompanyToDelete == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }

        if(recordCompanyToDelete.getProducts() != null && !recordCompanyToDelete.getProducts().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("RecordCompany cannot be deleted, it has products attached to it."));
        }

        recordCompanyRepository.delete(recordCompanyToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(recordCompanyToDelete));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateRecordCompany(@PathVariable int id, @RequestBody RecordCompany recordCompany) {
        RecordCompany recordCompanyToUpdate = findRecordCompany(id);
        if(recordCompanyToUpdate == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
        }


        if(recordCompany.getName() != null ) {
            recordCompanyToUpdate.setName(recordCompany.getName());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(recordCompanyRepository.save(recordCompanyToUpdate)));
    }

    private RecordCompany findRecordCompany(int id) {
        return recordCompanyRepository.findById(id).orElse(null);
    }

    private boolean containsNull(RecordCompany recordCompany) {
        return recordCompany.getName() == null;
    }
}

