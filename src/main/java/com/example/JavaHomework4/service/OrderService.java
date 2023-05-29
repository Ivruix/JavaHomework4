package com.example.JavaHomework4.service;

import com.example.JavaHomework4.dto.DishInfoDto;
import com.example.JavaHomework4.dto.NewDishDto;
import com.example.JavaHomework4.dto.OrderDto;
import com.example.JavaHomework4.dto.OrderInfoDto;
import com.example.JavaHomework4.exception.GetException;
import com.example.JavaHomework4.exception.OrderException;
import com.example.JavaHomework4.exception.RemoveException;
import com.example.JavaHomework4.model.*;
import com.example.JavaHomework4.repository.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {
    private final DishRepository dishRepository;
    private final OrderRepository orderRepository;
    private final OrderDishRepository orderDishRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public OrderService(DishRepository dishRepository, OrderRepository orderRepository, OrderDishRepository orderDishRepository, UserRepository userRepository, SessionRepository sessionRepository) {
        this.dishRepository = dishRepository;
        this.orderRepository = orderRepository;
        this.orderDishRepository = orderDishRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    // Метод для оформления заказа
    public void order(OrderDto orderDto) throws OrderException {
        // Ищем сессию по токену
        String token = orderDto.getToken();
        Session session = sessionRepository.findBySessionToken(token).orElseThrow(() -> new OrderException("Invalid token."));

        User user = userRepository.findById(session.getUserId()).orElseThrow(() -> new OrderException("User not found."));

        // Получаем список блюд и их количество из заказа
        var dishes = orderDto.getDishes();
        for (Map.Entry<Long, Long> entry : dishes.entrySet()) {
            // Проверяем наличие блюда и его количество в хранилище
            Dish dish = dishRepository.findById(entry.getKey()).orElseThrow(() -> new OrderException("Dish not found."));
            if (entry.getValue() > dish.getQuantity()) {
                throw new OrderException("Not enough items in the storage.");
            }
        }

        // Создаем заказ
        Order order = new Order();
        order.setUserId(user.getId());
        order.setStatus("В ожидании");
        order.setSpecialRequests(orderDto.getSpecialOrders());
        order = orderRepository.save(order);

        // Устанавливаем таймеры для изменения статуса заказа
        final Order finalOrder = order;
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                finalOrder.setStatus("В работе");
                orderRepository.save(finalOrder);
            }
        }, 3000);

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                finalOrder.setStatus("Выполнен");
                orderRepository.save(finalOrder);
            }
        }, 10000);

        // Создаем связи между заказом и блюдами
        for (Map.Entry<Long, Long> entry : dishes.entrySet()) {
            OrderDish orderDish = new OrderDish();
            orderDish.setOrderId(order.getId());
            orderDish.setDishId(entry.getKey());
            Dish dish = dishRepository.findById(entry.getKey()).orElseThrow(() -> new OrderException("Dish not found."));
            dish.setQuantity(dish.getQuantity() - entry.getValue());
            if (dish.getQuantity() == 0) {
                dish.setAvailable(false);
            }
            orderDish.setPrice(dish.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
            orderDish.setQuantity(entry.getValue());
            dishRepository.save(dish);
            orderDishRepository.save(orderDish);
        }
    }

    // Метод для добавления нового блюда
    public void addNewDish(NewDishDto newDishDto) {
        Dish dish = new Dish();
        dish.setName(newDishDto.getName());
        dish.setDescription(newDishDto.getDescription());
        dish.setPrice(newDishDto.getPrice());
        dish.setQuantity(newDishDto.getQuantity());
        dish.setAvailable(dish.getQuantity() != 0);

        dishRepository.save(dish);
    }

    // Метод для удаления блюда по идентификатору
    public void removeDish(Long id) throws RemoveException {
        if (dishRepository.existsById(id)) {
            dishRepository.deleteById(id);
        } else {
            throw new RemoveException("No dish with this id.");
        }
    }

    // Метод для получения информации о блюде по идентификатору
    public DishInfoDto getDish(Long id) throws GetException {
        Dish dish = dishRepository.findById(id).orElseThrow(() -> new GetException("No dish with this id."));
        DishInfoDto dishInfo = new DishInfoDto();

        dishInfo.setName(dish.getName());
        dishInfo.setDescription(dish.getDescription());
        dishInfo.setPrice(dish.getPrice());
        dishInfo.setQuantity(dish.getQuantity());
        dishInfo.setAvailable(dish.getAvailable());
        dishInfo.setCreatedAt(dish.getCreatedAt());
        dishInfo.setUpdatedAt(dish.getUpdatedAt());

        return dishInfo;
    }

    // Метод для получения меню с блюдами и их ценами
    public Map<String, BigDecimal> getMenu() {
        Map<String, BigDecimal> menu = new HashMap<>();
        for (Dish dish : dishRepository.findAll()) {
            if (dish.getAvailable()) {
                menu.put(dish.getName(), dish.getPrice());
            }
        }
        return menu;
    }

    // Метод для получения информации о заказе по идентификатору
    public OrderInfoDto getOrder(Long id) throws GetException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new GetException("No order with this id."));
        OrderInfoDto orderInfo = new OrderInfoDto();

        orderInfo.setStatus(order.getStatus());
        orderInfo.setSpecialRequests(order.getSpecialRequests());
        orderInfo.setCreatedAt(order.getCreatedAt());
        orderInfo.setUpdatedAt(order.getUpdatedAt());

        return orderInfo;
    }
}