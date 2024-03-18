package com.booleanuk.musicwebstorebackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "artists")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column
    private OffsetDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column
    private OffsetDateTime updatedAt;

//    @OneToMany(mappedBy = "artist")
//    @JsonIgnoreProperties("artist")
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private Product product;


   public Artist(String name) {
       this.name = name;
   }

   public Artist(int id) {
       this.id = id;
   }

    @PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
