package com.booleanuk.musicwebstorebackend.repository;

import com.booleanuk.musicwebstorebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import com.booleanuk.musicwebstorebackend.model.Order;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>{

    Optional<Order> findFirstByUserAndDate(User user, OffsetDateTime date);

    List<Order> findAllByUser(User user);

}
