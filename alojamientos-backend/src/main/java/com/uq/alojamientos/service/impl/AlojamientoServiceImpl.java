package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.dto.AlojamientoDTO;
import com.uq.alojamientos.exception.BusinessException;
import com.uq.alojamientos.model.Alojamiento;
import com.uq.alojamientos.model.Usuario;
import com.uq.alojamientos.repository.AlojamientoRepository;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.service.AlojamientoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlojamientoServiceImpl implements AlojamientoService {

  private final AlojamientoRepository repo;
  private final UsuarioRepository usuarioRepo;
  private final ModelMapper mm;

  @Override
  public AlojamientoDTO crear(AlojamientoDTO dto) {
    Usuario anfitrion = usuarioRepo.findById(dto.getAnfitrionId())
      .orElseThrow(() -> new BusinessException("Anfitrión no existe"));

    Alojamiento entity = mm.map(dto, Alojamiento.class);
    entity.setAnfitrion(anfitrion);
    entity.setEstado("ACTIVO");
    entity = repo.save(entity);
    return mm.map(entity, AlojamientoDTO.class);
  }

  @Override
  public Page<AlojamientoDTO> listarPorCiudad(String ciudad, int page, int size) {
    return repo.findActivosByCiudad(ciudad, PageRequest.of(page, size))
               .map(e -> mm.map(e, AlojamientoDTO.class));
  }

  @Override
  public void eliminarLogico(Long id) {
    Alojamiento a = repo.findById(id)
      .orElseThrow(() -> new BusinessException("Alojamiento no existe"));
    a.setEstado("ELIMINADO");
    repo.save(a);
  }
}
