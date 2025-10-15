package com.uq.alojamientos.service;

import com.uq.alojamientos.dto.ComentarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ComentarioService {
    ComentarioDTO crear(ComentarioDTO dto);
    Page<ComentarioDTO> listarPorAlojamiento(Long alojamientoId, Pageable pageable);
    Double promedioCalificacion(Long alojamientoId);
    void responder(Long comentarioId, String respuesta); // anfitrión
}
