// ==========================================
// ReservaController.java
// ==========================================
package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.ReservaDTO;
import com.uq.alojamientos.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@Validated
@Tag(name = "Reservas", description = "Gestión de reservas")
@SecurityRequirement(name = "Bearer Authentication")
public class ReservaController {

    private final ReservaService service;

    @Operation(summary = "Crear nueva reserva")
    @PreAuthorize("hasAnyRole('USER', 'ANFITRION', 'ADMIN')")
    @PostMapping
    public ResponseEntity<ReservaDTO> crear(@Valid @RequestBody ReservaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Cancelar reserva")
    @PreAuthorize("hasAnyRole('USER', 'ANFITRION', 'ADMIN')")
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        service.cancelar(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar reservas (por usuario o alojamiento)")
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public Page<ReservaDTO> listar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false) Long alojamientoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        var pageable = PageRequest.of(page, size);
        if (usuarioId != null) return service.listarPorUsuario(usuarioId, pageable);
        if (alojamientoId != null) return service.listarPorAlojamiento(alojamientoId, pageable);
        return Page.empty(pageable);
    }
}