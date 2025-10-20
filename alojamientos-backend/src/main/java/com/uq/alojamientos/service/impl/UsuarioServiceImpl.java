

// ==========================================
// UsuarioServiceImpl.java (Implementación completa)
// ==========================================
package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.domain.enums.RolUsuario;
import com.uq.alojamientos.dto.UsuarioDTO;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO registrar(UsuarioDTO dto) {
        // Validar que el email no exista
        if (repo.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario usuario = mapper.map(dto, Usuario.class);

        // Encriptar contraseña
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        // Asignar rol
        try {
            usuario.setRol(RolUsuario.valueOf(dto.getRol()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Rol inválido: " + dto.getRol());
        }

        // Activar usuario por defecto
        usuario.setActivo(true);

        usuario = repo.save(usuario);

        // No retornar el password en el DTO
        UsuarioDTO resultado = mapper.map(usuario, UsuarioDTO.class);
        resultado.setPassword(null);

        return resultado;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return repo.findAll().stream()
                .map(u -> {
                    UsuarioDTO dto = mapper.map(u, UsuarioDTO.class);
                    dto.setPassword(null); // No exponer contraseñas
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(Long id) {
        Usuario usuario = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + id));

        UsuarioDTO dto = mapper.map(usuario, UsuarioDTO.class);
        dto.setPassword(null);
        return dto;
    }
}