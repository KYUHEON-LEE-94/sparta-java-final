package com.ecommerce.gateway.controller;


import com.ecommerce.gateway.dto.LoginRequest;
import com.ecommerce.gateway.dto.LoginResponse;
import com.ecommerce.gateway.service.JwtTokenProvider;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        if ("test@example.com".equals(request.getEmail()) && "password".equals(request.getPassword())) {
            String token = jwtTokenProvider.createToken(request.getEmail(), List.of("ROLE_USER"));
            return ResponseEntity.ok(new LoginResponse(token));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
