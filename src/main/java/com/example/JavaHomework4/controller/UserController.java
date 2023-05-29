package com.example.JavaHomework4.controller;

import com.example.JavaHomework4.dto.UserInfoDto;
import com.example.JavaHomework4.dto.UserLoginDto;
import com.example.JavaHomework4.dto.UserRegistrationDto;
import com.example.JavaHomework4.exception.AuthenticationException;
import com.example.JavaHomework4.exception.RegistrationException;
import com.example.JavaHomework4.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Метод для регистрации пользователя
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Validated @RequestBody UserRegistrationDto registrationDto) {
        try {
            userService.registerUser(registrationDto);
            return ResponseEntity.ok("User registered successfully.");
        } catch (RegistrationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Метод для входа пользователя в систему
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Validated @RequestBody UserLoginDto loginDto) {
        try {
            String token = userService.loginUser(loginDto);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // Метод для получения информации о пользователе
    @GetMapping("/info")
    public ResponseEntity<UserInfoDto> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            UserInfoDto userInfo = userService.getUserInfo(token);
            return ResponseEntity.ok(userInfo);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}

