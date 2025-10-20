// ==========================================
// AlojamientoServiceImpl.java (con @Transactional)
// ==========================================
package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.domain.Alojamiento;
import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.domain.enums.EstadoAlojamiento;
import com.uq.alojamientos.domain.enums.EstadoReserva;
import com.uq.alojamientos.dto.AlojamientoDTO;
import com.uq.alojamientos.repository.AlojamientoRepository;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.service.AlojamientoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
@Transactional
public class AlojamientoServiceImpl implements AlojamientoService {

    private final AlojamientoRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final ModelMapper mapper;

    @Override
    public AlojamientoDTO crear(AlojamientoDTO dto) {
        // Validar que el anfitrión existe
        Usuario anfitrion = usuarioRepo.findById(dto.getAnfitrionId())
                .orElseThrow(() -> new IllegalArgumentException("Anfitrión no encontrado con ID: " + dto.getAnfitrionId()));

        Alojamiento entity = mapper.map(dto, Alojamiento.class);
        entity.setAnfitrion(anfitrion);
        entity.setEstado(EstadoAlojamiento.ACTIVO);

        // Manejar servicios
        if (dto.getServicios() != null && !dto.getServicios().isEmpty()) {
            entity.setServiciosLista(dto.getServicios());
        }

        entity = repo.save(entity);
        return mapToDTO(entity);
    }

    @Override
    public void eliminarLogico(Long id) {
        Alojamiento alojamiento = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alojamiento no encontrado con ID: " + id));

        alojamiento.setEstado(EstadoAlojamiento.ELIMINADO);
        repo.save(alojamiento);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlojamientoDTO> buscarActivosPorCiudad(String ciudad, Pageable pageable) {
        return repo.findByEstadoAndCiudadContainingIgnoreCase(
                        EstadoAlojamiento.ACTIVO, ciudad, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlojamientoDTO> buscarDisponibles(
            String ciudad,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Integer capacidad,
            LocalDate desde,
            LocalDate hasta,
            Pageable pageable
    ) {
        var activos = EnumSet.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA);
        return repo.buscarDisponibles(
                        ciudad, precioMin, precioMax, capacidad, desde, hasta, activos, pageable)
                .map(this::mapToDTO);
    }

    private AlojamientoDTO mapToDTO(Alojamiento entity) {
        AlojamientoDTO dto = mapper.map(entity, AlojamientoDTO.class);
        if (entity.getAnfitrion() != null) {
            dto.setAnfitrionId(entity.getAnfitrion().getId());
        }
        dto.setServicios(entity.getServiciosLista());
        return dto;
    }
}