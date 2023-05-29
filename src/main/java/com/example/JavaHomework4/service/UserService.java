package com.example.JavaHomework4.service;

import com.example.JavaHomework4.dto.UserInfoDto;
import com.example.JavaHomework4.dto.UserLoginDto;
import com.example.JavaHomework4.dto.UserRegistrationDto;
import com.example.JavaHomework4.exception.AuthenticationException;
import com.example.JavaHomework4.exception.RegistrationException;
import com.example.JavaHomework4.model.Session;
import com.example.JavaHomework4.model.User;
import com.example.JavaHomework4.repository.SessionRepository;
import com.example.JavaHomework4.repository.UserRepository;
import com.example.JavaHomework4.util.HashUtil;
import com.example.JavaHomework4.util.JwtTokenUtil;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final HashUtil hashUtil;

    public UserService(UserRepository userRepository, SessionRepository sessionRepository,
                       JwtTokenUtil jwtTokenUtil, HashUtil hashUtil) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.hashUtil = hashUtil;
    }

    // Метод для регистрации нового пользователя
    public void registerUser(UserRegistrationDto registrationDto) throws RegistrationException {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RegistrationException("Email is already taken.");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPasswordHash(hashUtil.hashPassword(registrationDto.getPassword()));
        if (registrationDto.getRole() == null) {
            user.setRole("customer");
        } else {
            user.setRole(registrationDto.getRole());
        }

        userRepository.save(user);
    }

    // Метод для входа пользователя
    public String loginUser(UserLoginDto loginDto) throws AuthenticationException {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password."));

        if (!user.getPasswordHash().equals(hashUtil.hashPassword(loginDto.getPassword()))) {
            throw new AuthenticationException("Invalid email or password.");
        }

        String token = jwtTokenUtil.generateToken(user);

        Session session = new Session();
        session.setUserId(user.getId());
        session.setSessionToken(token);
        session.setExpiresAt(LocalDateTime.now().plusDays(1));

        sessionRepository.save(session);

        return token;
    }

    // Метод для получения информации о пользователе при наличии токена
    public UserInfoDto getUserInfo(String token) throws AuthenticationException {
        Session session = sessionRepository.findBySessionToken(token)
                .orElseThrow(() -> new AuthenticationException("Invalid token."));
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Invalid token.");
        }

        User user = userRepository.findById(session.getUserId())
                .orElseThrow(() -> new AuthenticationException("User not found."));

        UserInfoDto userInfo = new UserInfoDto();
        userInfo.setUsername(user.getUsername());
        userInfo.setRole(user.getRole());

        return userInfo;
    }
}
