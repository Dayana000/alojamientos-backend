// ==========================================
// ComentarioController.java
// ==========================================
package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.ComentarioDTO;
import com.uq.alojamientos.dto.ResponderComentarioDTO;
import com.uq.alojamientos.service.ComentarioService;
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
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
@Validated
@Tag(name = "Comentarios", description = "Gestión de comentarios y calificaciones")
public class ComentarioController {

    private final ComentarioService service;

    @Operation(
            summary = "Crear comentario",
            description = "Solo para reservas COMPLETADAS y 1 comentario por reserva",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ComentarioDTO> crear(@Valid @RequestBody ComentarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar comentarios de un alojamiento (paginado)")
    @GetMapping("/alojamiento/{alojamientoId}")
    public Page<ComentarioDTO> listarPorAlojamiento(
            @PathVariable Long alojamientoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.listarPorAlojamiento(alojamientoId, PageRequest.of(page, size));
    }

    @Operation(summary = "Obtener promedio de calificaciones")
    @GetMapping("/alojamiento/{alojamientoId}/promedio")
    public ResponseEntity<Double> promedio(@PathVariable Long alojamientoId) {
        return ResponseEntity.ok(service.promedioCalificacion(alojamientoId));
    }

    @Operation(
            summary = "Responder comentario (anfitrión)",
            security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @PreAuthorize("hasAnyRole('ANFITRION', 'ADMIN')")
    @PostMapping("/{comentarioId}/respuesta")
    public ResponseEntity<Void> responder(
            @PathVariable Long comentarioId,
            @Valid @RequestBody ResponderComentarioDTO body
    ) {
        service.responder(comentarioId, body.getRespuesta());
        return ResponseEntity.ok().build();
    }
}