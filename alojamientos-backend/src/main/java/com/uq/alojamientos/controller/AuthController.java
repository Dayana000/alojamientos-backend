package com.uq.alojamientos.controller;


import com.uq.alojamientos.dto.auth.LoginRequest;
import com.uq.alojamientos.dto.auth.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Operation(summary = "Login (demo)")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        // Demo: siempre retorna un token dummy
        return ResponseEntity.ok(new LoginResponse("demo-token-123"));
    }
}
