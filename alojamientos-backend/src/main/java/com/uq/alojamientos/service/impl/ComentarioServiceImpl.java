package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.dto.ComentarioDTO;
import com.uq.alojamientos.exception.BusinessException;
import com.uq.alojamientos.model.Alojamiento;
import com.uq.alojamientos.model.Comentario;
import com.uq.alojamientos.model.Usuario;
import com.uq.alojamientos.repository.AlojamientoRepository;
import com.uq.alojamientos.repository.ComentarioRepository;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {

  private final ComentarioRepository repo;
  private final UsuarioRepository usuarioRepo;
  private final AlojamientoRepository alojamientoRepo;
  private final ModelMapper mm;

  @Override
  public ComentarioDTO crear(ComentarioDTO dto) {
    Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
      .orElseThrow(() -> new BusinessException("Usuario no existe"));
    Alojamiento alojamiento = alojamientoRepo.findById(dto.getAlojamientoId())
      .orElseThrow(() -> new BusinessException("Alojamiento no existe"));

    if (dto.getCalificacion() == null || dto.getCalificacion() < 1 || dto.getCalificacion() > 5) {
      throw new BusinessException("Calificación debe estar entre 1 y 5");
    }

    Comentario c = mm.map(dto, Comentario.class);
    c.setUsuario(usuario);
    c.setAlojamiento(alojamiento);
    if (c.getFechaCreacion() == null) c.setFechaCreacion(LocalDateTime.now());
    c = repo.save(c);
    return mm.map(c, ComentarioDTO.class);
  }
}
