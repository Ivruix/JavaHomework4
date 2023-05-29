package com.example.JavaHomework4.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class OrderDto {
    @NotNull
    private String token;
    @NotNull
    private Map<Long, Long> dishes;
    @NotNull
    private String specialOrders;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<Long, Long> getDishes() {
        return dishes;
    }

    public void setDishes(Map<Long, Long> dishes) {
        this.dishes = dishes;
    }

    public String getSpecialOrders() {
        return specialOrders;
    }

    public void setSpecialOrders(String specialOrders) {
        this.specialOrders = specialOrders;
    }
}
