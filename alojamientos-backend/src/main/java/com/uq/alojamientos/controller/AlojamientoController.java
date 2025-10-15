package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.AlojamientoDTO;
import com.uq.alojamientos.service.AlojamientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/alojamientos")
@RequiredArgsConstructor
public class AlojamientoController {

    private final AlojamientoService service;

    @PostMapping
    public AlojamientoDTO crear(@RequestBody AlojamientoDTO dto) {
        return service.crear(dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminarLogico(id);
    }

    @GetMapping
    public Page<AlojamientoDTO> listar(
            @RequestParam(defaultValue = "") String ciudad,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.buscarActivosPorCiudad(ciudad, PageRequest.of(page, size));
    }

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
                ciudad, precioMin, precioMax, capacidad, desde, hasta, PageRequest.of(page, size));
    }
}
