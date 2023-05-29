package com.example.JavaHomework4.repository;

import com.example.JavaHomework4.model.Dish;
import com.example.JavaHomework4.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
