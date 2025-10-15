package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.auth.LoginRequest;
import com.uq.alojamientos.dto.auth.LoginResponse;
import com.uq.alojamientos.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Operation(summary = "Login y obtención de token JWT")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        // Autentica usuario y contraseña con el AuthenticationManager
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        var user = (UserDetails) auth.getPrincipal();

        // Genera token JWT con los roles del usuario
        String token = jwtService.generate(
                user.getUsername(),
                Map.of("roles", user.getAuthorities().stream().map(Object::toString).toList())
        );

        return new LoginResponse(token, "Bearer");
    }
}
