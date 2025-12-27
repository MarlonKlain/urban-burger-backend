package com.example.urban_burger.service;

import com.example.urban_burger.controller.auth.AuthenticationRequest;
import com.example.urban_burger.controller.auth.AuthenticationResponse;
import com.example.urban_burger.controller.auth.RegisterRequest;
import com.example.urban_burger.entity.User;
import com.example.urban_burger.repository.UserRepository;
import com.example.urban_burger.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                var user = User.builder()
                                .login(request.getLogin())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role("USER")
                                .build();
                repository.save(user);
                var jwtToken = jwtUtil.generateToken(user.getUsername());
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtUtil.generateToken(user.getUsername());
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }
}
