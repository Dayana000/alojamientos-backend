package com.uq.alojamientos.controller;

import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.domain.enums.RolUsuario;
import com.uq.alojamientos.dto.auth.LoginRequest;
import com.uq.alojamientos.dto.auth.RegisterRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // =========================================================
    // LOGIN
    // =========================================================
    @Operation(summary = "Autenticación y obtención de token JWT")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getEmail(),
                            req.getPassword()
                    )
            );

            UserDetails user = (UserDetails) authentication.getPrincipal();

            String token = jwtService.generate(
                    user.getUsername(),
                    Map.of(
                            "roles",
                            user.getAuthorities()
                                    .stream()
                                    .map(Object::toString)
                                    .toList()
                    )
            );

            // Formato que espera tu frontend (accessToken + tokenType)
            return ResponseEntity.ok(
                    Map.of(
                            "accessToken", token,
                            "tokenType", "Bearer"
                    )
            );

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

    // =========================================================
    // REGISTRO
    // =========================================================
    @Operation(summary = "Registro de nuevos usuarios")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        // 1) Validar si ya existe un usuario con ese correo
        if (usuarioRepository.existsByEmail(req.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Ya existe un usuario con ese correo"));
        }

        try {
            // 2) Crear entidad Usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(req.getNombre());
            usuario.setEmail(req.getEmail());
            usuario.setTelefono(req.getTelefono());
            usuario.setActivo(true); // por defecto activo

            // ---- Fecha de nacimiento ----
            // Si en RegisterRequest la fecha es LocalDate, esto está bien:
            usuario.setFechaNacimiento(req.getFechaNacimiento());

            // Si en RegisterRequest es String, usa esto en su lugar:
            // LocalDate fecha = LocalDate.parse(req.getFechaNacimiento());
            // usuario.setFechaNacimiento(fecha);

            // ---- Rol (USER / ANFITRION) ----
            try {
                RolUsuario rol = RolUsuario.valueOf(req.getRol().toUpperCase());
                usuario.setRol(rol);
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El rol debe ser USER o ANFITRION"));
            }

            // ---- Encriptar contraseña ----
            usuario.setPasswordHash(passwordEncoder.encode(req.getPassword()));

            // 3) Guardar en BD
            usuarioRepository.save(usuario);

            // 4) Respuesta OK
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Usuario registrado correctamente"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "No se pudo registrar el usuario",
                            "detalle", e.getMessage()
                    ));
        }
    }
}
