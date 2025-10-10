package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.ComentarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    @Operation(summary = "Crear comentario")
    @PostMapping
    public ResponseEntity<ComentarioDTO> crear(@RequestBody ComentarioDTO dto) {
        // TODO: servicio.crear(dto)
        dto.setId(1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
