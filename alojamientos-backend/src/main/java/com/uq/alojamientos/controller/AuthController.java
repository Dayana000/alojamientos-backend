package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.auth.LoginRequest;
import com.uq.alojamientos.dto.auth.LoginResponse;
import com.uq.alojamientos.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Autenticación y obtención de token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generate(user.getUsername(), Map.of("roles", user.getAuthorities().stream().map(Object::toString).toList()));

            LoginResponse response = new LoginResponse(token, "Bearer");
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error","Credenciales incorrectas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","Error interno","detalle",e.getMessage()));
        }
    }
}
