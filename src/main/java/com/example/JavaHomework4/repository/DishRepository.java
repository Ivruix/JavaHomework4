package com.example.JavaHomework4.repository;

import com.example.JavaHomework4.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {

}