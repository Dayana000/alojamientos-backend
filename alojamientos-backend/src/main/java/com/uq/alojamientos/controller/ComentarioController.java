package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.ComentarioDTO;
import com.uq.alojamientos.dto.ResponderComentarioDTO;
import com.uq.alojamientos.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comentarios")
@RequiredArgsConstructor
public class ComentarioController {

    private final ComentarioService service;

    @Operation(summary = "Crear comentario (solo reservas COMPLETADAS y 1 por reserva)")
    @PostMapping
    public ResponseEntity<ComentarioDTO> crear(@RequestBody ComentarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar comentarios de un alojamiento (paginado, más recientes primero)")
    @GetMapping("/alojamiento/{alojamientoId}")
    public Page<ComentarioDTO> listarPorAlojamiento(@PathVariable Long alojamientoId,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return service.listarPorAlojamiento(alojamientoId, PageRequest.of(page, size));
    }

    @Operation(summary = "Promedio de calificaciones por alojamiento")
    @GetMapping("/alojamiento/{alojamientoId}/promedio")
    public Double promedio(@PathVariable Long alojamientoId) {
        return service.promedioCalificacion(alojamientoId);
    }

    @Operation(summary = "Responder comentario (anfitrión)")
    @PostMapping("/{comentarioId}/respuesta")
    public void responder(@PathVariable Long comentarioId, @RequestBody ResponderComentarioDTO body) {
        service.responder(comentarioId, body.getRespuesta());
    }
}
