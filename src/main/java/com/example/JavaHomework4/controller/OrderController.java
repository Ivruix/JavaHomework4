package com.example.JavaHomework4.controller;

import com.example.JavaHomework4.dto.*;
import com.example.JavaHomework4.exception.GetException;
import com.example.JavaHomework4.exception.OrderException;
import com.example.JavaHomework4.exception.RemoveException;
import com.example.JavaHomework4.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/order/")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Метод для регистрации заказа
    @PostMapping("/order")
    public ResponseEntity<String> registerUser(@Validated @RequestBody OrderDto orderDto) {
        try {
            orderService.order(orderDto);
            return ResponseEntity.ok("Ordered successfully.");
        } catch (OrderException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Метод для добавления нового блюда
    @PostMapping("/new_dish")
    public ResponseEntity<String> addDish(@RequestBody NewDishDto newDishDto) {
        try {
            orderService.addNewDish(newDishDto);
            return ResponseEntity.ok("Dish added successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Метод для удаления блюда
    @DeleteMapping("/remove_dish")
    public ResponseEntity<String> removeDish(@RequestParam Long id) {
        try {
            orderService.removeDish(id);
            return ResponseEntity.ok("Dish removed successfully.");
        } catch (RemoveException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Метод для получения информации о блюде
    @GetMapping("/get_dish")
    public ResponseEntity<DishInfoDto> getDish(@RequestParam Long id) {
        try {
            DishInfoDto dishInfo = orderService.getDish(id);
            return ResponseEntity.ok(dishInfo);
        } catch (GetException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Метод для получения информации о заказе
    @GetMapping("/get_order")
    public ResponseEntity<OrderInfoDto> getOrder(@RequestParam Long id) {
        try {
            OrderInfoDto orderInfo = orderService.getOrder(id);
            return ResponseEntity.ok(orderInfo);
        } catch (GetException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Метод для получения меню
    @GetMapping("/get_menu")
    public ResponseEntity<Map<String, BigDecimal>> getMenu() {
        try {
            Map<String, BigDecimal> dishInfo = orderService.getMenu();
            return ResponseEntity.ok(dishInfo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
