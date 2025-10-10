package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.ReservaDTO;
import com.uq.alojamientos.model.enums.EstadoReserva;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    @Operation(summary = "Crear reserva")
    @PostMapping
    public ResponseEntity<ReservaDTO> crear(@RequestBody ReservaDTO dto) {
        // TODO: validaciones/servicio
        if (dto.getEstado() == null) dto.setEstado(EstadoReserva.PENDIENTE);
        dto.setId(1L);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
