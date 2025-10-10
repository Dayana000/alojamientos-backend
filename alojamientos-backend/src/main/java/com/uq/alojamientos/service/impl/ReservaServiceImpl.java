package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.dto.ReservaDTO;
import com.uq.alojamientos.exception.BusinessException;
import com.uq.alojamientos.model.Alojamiento;
import com.uq.alojamientos.model.Reserva;
import com.uq.alojamientos.model.Usuario;
import com.uq.alojamientos.model.enums.EstadoReserva;
import com.uq.alojamientos.repository.AlojamientoRepository;
import com.uq.alojamientos.repository.ReservaRepository;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

  private final ReservaRepository repo;
  private final UsuarioRepository usuarioRepo;
  private final AlojamientoRepository alojamientoRepo;
  private final ModelMapper mm;

  @Override
  public ReservaDTO crear(ReservaDTO dto) {
    Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
      .orElseThrow(() -> new BusinessException("Usuario no existe"));
    Alojamiento alojamiento = alojamientoRepo.findById(dto.getAlojamientoId())
      .orElseThrow(() -> new BusinessException("Alojamiento no existe"));

    if (!dto.getCheckIn().isBefore(dto.getCheckOut())) {
      throw new BusinessException("Rango de fechas inválido");
    }

    Reserva r = mm.map(dto, Reserva.class);
    r.setUsuario(usuario);
    r.setAlojamiento(alojamiento);
    r.setEstado(EstadoReserva.PENDIENTE);
    r = repo.save(r);
    return mm.map(r, ReservaDTO.class);
  }
}
