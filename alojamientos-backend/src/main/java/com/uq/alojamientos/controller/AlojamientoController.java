package com.uq.alojamientos.controller;


import com.uq.alojamientos.dto.AlojamientoDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alojamientos")
@RequiredArgsConstructor
public class AlojamientoController {

    @Operation(summary = "Crear alojamiento")
    @PostMapping
    public ResponseEntity<AlojamientoDTO> crear(@RequestBody AlojamientoDTO dto) {
        // TODO: llamar a servicio real
        dto.setId(1L);
        if (dto.getEstado() == null) dto.setEstado("ACTIVO");
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @Operation(summary = "Eliminar (soft delete) alojamiento")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        // TODO: servicio.eliminarLogico(id)
        return ResponseEntity.noContent().build();
    }
}
