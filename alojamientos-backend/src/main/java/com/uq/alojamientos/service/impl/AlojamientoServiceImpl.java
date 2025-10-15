package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.domain.Alojamiento;
import com.uq.alojamientos.domain.enums.EstadoAlojamiento;
import com.uq.alojamientos.domain.enums.EstadoReserva;
import com.uq.alojamientos.dto.AlojamientoDTO;
import com.uq.alojamientos.repository.AlojamientoRepository;
import com.uq.alojamientos.service.AlojamientoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.EnumSet;

@Service
@RequiredArgsConstructor
public class AlojamientoServiceImpl implements AlojamientoService {

    private final AlojamientoRepository repo;
    private final ModelMapper mapper;

    @Override
    public AlojamientoDTO crear(AlojamientoDTO dto) {
        Alojamiento entity = mapper.map(dto, Alojamiento.class);
        entity.setEstado(EstadoAlojamiento.ACTIVO);
        entity = repo.save(entity);
        return mapper.map(entity, AlojamientoDTO.class);
    }

    @Override
    public void eliminarLogico(Long id) {
        Alojamiento a = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alojamiento no existe"));
        a.setEstado(EstadoAlojamiento.ELIMINADO);
        repo.save(a);
    }

    @Override
    public Page<AlojamientoDTO> buscarActivosPorCiudad(String ciudad, Pageable pageable) {
        return repo.findByEstadoAndCiudadContainingIgnoreCase(
                        EstadoAlojamiento.ACTIVO, ciudad, pageable)
                .map(e -> {
                    var d = mapper.map(e, AlojamientoDTO.class);
                    if (e.getAnfitrion() != null) d.setAnfitrionId(e.getAnfitrion().getId());
                    return d;
                });
    }

    @Override
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
                .map(e -> {
                    var d = mapper.map(e, AlojamientoDTO.class);
                    if (e.getAnfitrion() != null) d.setAnfitrionId(e.getAnfitrion().getId());
                    return d;
                });
    }
}
