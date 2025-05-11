package com.secureauth.verification.service;

import com.secureauth.verification.model.User;
import com.secureauth.verification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updatePhoneNumber(String login, String phoneNumber) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    public Map<String, Object> getUserProfile(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return Map.of(
                "login", user.getLogin(),
                "email", user.getEmail(),
                "phoneNumber", user.getPhoneNumber(),
                "telegramChatId", user.getTelegramChatId()
        );
    }
} 