package com.example.JavaHomework4.repository;

import com.example.JavaHomework4.model.Dish;
import com.example.JavaHomework4.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findBySessionToken(String email);
}