package com.booleanuk.musicwebstorebackend.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.OffsetDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    private OffsetDateTime date;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "createdAt")
    private OffsetDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "updatedAt")
    private OffsetDateTime updatedAt;


    @OneToMany(mappedBy = "order")
  @JsonIgnoreProperties("order")
 // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<OrderLine> orderLine;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIncludeProperties(value = {"name", "email", "password"})
    private User user;


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

    public Order(OffsetDateTime date) {
        this.date = date;
    }

    public Order(int id) {
        this.id = id;
    }

}
