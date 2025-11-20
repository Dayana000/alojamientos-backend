package com.uq.alojamientos.controller;

import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.dto.auth.LoginRequest;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final UsuarioRepository usuarioRepository;

    // =========================================================
    // LOGIN CORREGIDO
    // =========================================================
    @Operation(summary = "Autenticación y obtención de token + usuario")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            // Autenticar credenciales
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()
                    )
            );

            UserDetails springUser = (UserDetails) authentication.getPrincipal();

            // Buscar el usuario real en BD para retornarlo al frontend
            Usuario usuario = usuarioRepository.findByEmail(req.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado en BD"));

            // Generar token JWT
            String token = jwtService.generate(
                    usuario.getEmail(),
                    Map.of(
                            "roles",
                            springUser.getAuthorities()
                                    .stream()
                                    .map(Object::toString)
                                    .toList()
                    )
            );

            // Crear respuesta completa
            Map<String, Object> response = Map.of(
                    "accessToken", token,
                    "tokenType", "Bearer",
                    "usuarioDto", Map.of(
                            "id", usuario.getId(),
                            "nombre", usuario.getNombre(),
                            "email", usuario.getEmail(),
                            "telefono", usuario.getTelefono(),
                            "rol", usuario.getRol().name()
                    )
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Error interno",
                            "detalle", e.getMessage()
                    ));
        }
    }
}
