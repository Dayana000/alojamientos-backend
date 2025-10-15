package com.uq.alojamientos.controller;

import com.uq.alojamientos.dto.ReservaDTO;
import com.uq.alojamientos.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService service;

    @PostMapping
    public ReservaDTO crear(@RequestBody @Valid ReservaDTO dto) {
        return service.crear(dto);
    }

    @PutMapping("/{id}/cancelar")
    public void cancelar(@PathVariable Long id) {
        service.cancelar(id);
    }

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
