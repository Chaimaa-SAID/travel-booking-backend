package com.travel.userservice.service;

import com.travel.userservice.dto.AuthResponse;
import com.travel.userservice.dto.LoginRequest;
import com.travel.userservice.dto.RegisterRequest;
import com.travel.userservice.model.User;
import com.travel.userservice.repository.UserRepository;
import com.travel.userservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository repo, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public void register(RegisterRequest req) {

        String role = req.getRole();

        if (role == null || role.isBlank()) {
            role = "USER";
        }

        // sécurité minimale
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new RuntimeException("Rôle invalide");
        }

        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .role(role)
                .build();

        repo.save(user);
    }


    public AuthResponse login(LoginRequest req) {
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));



        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return new AuthResponse(
                jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole())
        );

    }
}
