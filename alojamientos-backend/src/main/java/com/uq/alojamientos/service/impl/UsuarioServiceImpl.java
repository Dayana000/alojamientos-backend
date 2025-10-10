package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.dto.UsuarioDTO;
import com.uq.alojamientos.model.Usuario;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

  private final UsuarioRepository repo;
  private final ModelMapper mm;

  @Override
  public UsuarioDTO registrar(UsuarioDTO dto) {
    Usuario u = mm.map(dto, Usuario.class);
    u.setPasswordHash("**Fase1_dummy**"); // Placeholder en Fase 1
    u = repo.save(u);
    return mm.map(u, UsuarioDTO.class);
  }
}
