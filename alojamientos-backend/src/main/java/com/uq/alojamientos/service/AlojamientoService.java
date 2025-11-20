package com.uq.alojamientos.service;

import com.uq.alojamientos.dto.AlojamientoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface AlojamientoService {

    AlojamientoDTO crear(AlojamientoDTO dto);

    void eliminarLogico(Long id);

    Page<AlojamientoDTO> buscarActivosPorCiudad(String ciudad, Pageable pageable);

    Page<AlojamientoDTO> buscarDisponibles(
            String ciudad,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Integer capacidad,
            LocalDate desde,
            LocalDate hasta,
            Pageable pageable
    );

    // 👇 ESTE ES EL MÉTODO QUE NECESITA EL CONTROLADOR
    AlojamientoDTO obtenerPorId(Long id);
}
