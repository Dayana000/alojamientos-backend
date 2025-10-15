package com.uq.alojamientos.service;

import com.uq.alojamientos.dto.ReservaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReservaService {
    ReservaDTO crear(ReservaDTO dto);
    void cancelar(Long id);
    Page<ReservaDTO> listarPorUsuario(Long usuarioId, Pageable pageable);
    Page<ReservaDTO> listarPorAlojamiento(Long alojamientoId, Pageable pageable);
}
