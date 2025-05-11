package com.secureauth.verification.service;

import com.secureauth.verification.dto.LoginRequest;
import com.secureauth.verification.dto.RegisterRequest;
import com.secureauth.verification.model.User;
import com.secureauth.verification.repository.UserRepository;
import com.secureauth.verification.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.findByLogin(request.login()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setLogin(request.login());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getLogin());
        return jwtService.generateToken(userDetails);
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.login(),
                        request.password()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.login());
        return jwtService.generateToken(userDetails);
    }
} 