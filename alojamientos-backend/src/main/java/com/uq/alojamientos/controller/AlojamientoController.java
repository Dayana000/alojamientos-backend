package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.AlojamientoDTO;
import com.uq.alojamientos.service.AlojamientoService;
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

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/alojamientos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Alojamientos", description = "Gestión de alojamientos")
public class AlojamientoController {

    private final AlojamientoService service;

    @Operation(
            summary = "Crear nuevo alojamiento",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ANFITRION','ADMIN')")
    @PostMapping
    public ResponseEntity<AlojamientoDTO> crear(@Valid @RequestBody AlojamientoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(
            summary = "Eliminar alojamiento (lógico)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ANFITRION','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar alojamientos activos por ciudad")
    @GetMapping
    public Page<AlojamientoDTO> listar(
            @RequestParam(defaultValue = "") String ciudad,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.buscarActivosPorCiudad(ciudad, PageRequest.of(page, size));
    }

    @Operation(summary = "Buscar alojamientos disponibles por fechas")
    @GetMapping("/disponibles")
    public Page<AlojamientoDTO> disponibles(
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax,
            @RequestParam(required = false) Integer capacidad,
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.buscarDisponibles(
                ciudad, precioMin, precioMax, capacidad, desde, hasta, PageRequest.of(page, size)
        );
    }

    @Operation(summary = "Obtener detalle de un alojamiento por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AlojamientoDTO> obtenerPorId(@PathVariable Long id) {
        AlojamientoDTO dto = service.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }
}
