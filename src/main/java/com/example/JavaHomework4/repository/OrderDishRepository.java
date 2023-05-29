package com.example.JavaHomework4.repository;

import com.example.JavaHomework4.model.Dish;
import com.example.JavaHomework4.model.OrderDish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderDishRepository extends JpaRepository<OrderDish, Long> {

}