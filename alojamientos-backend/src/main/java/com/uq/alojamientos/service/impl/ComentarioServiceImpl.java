package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.domain.Alojamiento;
import com.uq.alojamientos.domain.Comentario;
import com.uq.alojamientos.domain.Reserva;
import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.domain.enums.EstadoReserva;
import com.uq.alojamientos.dto.ComentarioDTO;
import com.uq.alojamientos.repository.AlojamientoRepository;
import com.uq.alojamientos.repository.ComentarioRepository;
import com.uq.alojamientos.repository.ReservaRepository;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.service.ComentarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepo;
    private final ReservaRepository reservaRepo;
    private final AlojamientoRepository alojamientoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ModelMapper mapper;

    @Override
    public ComentarioDTO crear(ComentarioDTO dto) {
        // Entidades base
        Reserva reserva = reservaRepo.findById(dto.getReservaId())
                .orElseThrow(() -> new RuntimeException("Reserva no existe"));
        Alojamiento alojamiento = alojamientoRepo.findById(dto.getAlojamientoId())
                .orElseThrow(() -> new RuntimeException("Alojamiento no existe"));
        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        // Reglas de negocio
        if (reserva.getEstado() != EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("Solo puedes comentar reservas COMPLETADAS");
        }
        if (comentarioRepo.existsByReservaId(reserva.getId())) {
            throw new IllegalStateException("Ya existe un comentario para esta reserva");
        }
        if (dto.getCalificacion() == null || dto.getCalificacion() < 1 || dto.getCalificacion() > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }

        Comentario entity = mapper.map(dto, Comentario.class);
        entity.setReserva(reserva);
        entity.setAlojamiento(alojamiento);
        entity.setUsuario(usuario);

        entity = comentarioRepo.save(entity);
        return mapper.map(entity, ComentarioDTO.class);
    }

    @Override
    public Page<ComentarioDTO> listarPorAlojamiento(Long alojamientoId, Pageable pageable) {
        return comentarioRepo.findByAlojamientoIdOrderByCreatedAtDesc(alojamientoId, pageable)
                .map(c -> mapper.map(c, ComentarioDTO.class));
    }

    @Override
    public Double promedioCalificacion(Long alojamientoId) {
        Double avg = comentarioRepo.promedioPorAlojamiento(alojamientoId);
        return (avg == null) ? 0.0 : Math.round(avg * 10.0) / 10.0; // redondeo a 1 decimal
    }

    @Override
    public void responder(Long comentarioId, String respuesta) {
        Comentario c = comentarioRepo.findById(comentarioId)
                .orElseThrow(() -> new RuntimeException("Comentario no existe"));
        c.setRespuestaAnfitrion(respuesta);
        comentarioRepo.save(c);
    }
}
