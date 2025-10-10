package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de ejemplo para que Swagger muestre operaciones.
 * Reemplaza los TODO con llamadas reales a UsuarioService cuando lo tengas.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    @Operation(summary = "Registrar usuario")
    @PostMapping
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody UsuarioDTO dto) {
        // TODO: usar UsuarioService para guardar en DB
        dto.setId(1L); // respuesta de ejemplo
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Obtener usuario por id (demo)")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable Long id) {
        // TODO: usar UsuarioService.findById(id)
        UsuarioDTO demo = new UsuarioDTO();
        demo.setId(id);
        demo.setNombre("Demo");
        demo.setEmail("demo@correo.com");
        demo.setRol("USUARIO");
        return ResponseEntity.ok(demo);
    }
}
