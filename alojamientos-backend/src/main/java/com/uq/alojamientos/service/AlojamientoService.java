package com.uq.alojamientos.service;

import com.uq.alojamientos.dto.AlojamientoDTO;
import org.springframework.data.domain.Page;

public interface AlojamientoService {
  AlojamientoDTO crear(AlojamientoDTO dto);
  Page<AlojamientoDTO> listarPorCiudad(String ciudad, int page, int size);
  void eliminarLogico(Long id);
}
