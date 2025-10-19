package com.uq.alojamientos.controller;

import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        // 🔐 Encriptar contraseña antes de guardar
        usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        Usuario nuevo = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }
}
