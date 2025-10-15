package com.uq.alojamientos.service.impl;

import com.uq.alojamientos.domain.Alojamiento;
import com.uq.alojamientos.domain.Reserva;
import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.domain.enums.EstadoReserva;
import com.uq.alojamientos.dto.ReservaDTO;
import com.uq.alojamientos.repository.AlojamientoRepository;
import com.uq.alojamientos.repository.ReservaRepository;
import com.uq.alojamientos.repository.UsuarioRepository;
import com.uq.alojamientos.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepo;
    private final AlojamientoRepository alojamientoRepo;
    private final UsuarioRepository usuarioRepo;
    private final ModelMapper mapper;

    private static final Set<EstadoReserva> ESTADOS_ACTIVOS =
            EnumSet.of(EstadoReserva.PENDIENTE, EstadoReserva.CONFIRMADA);

    @Override
    public ReservaDTO crear(ReservaDTO dto) {
        if (dto.getCheckIn() == null || dto.getCheckOut() == null ||
                !dto.getCheckIn().isBefore(dto.getCheckOut())) {
            throw new IllegalArgumentException("Rango de fechas inválido");
        }
        if (dto.getCheckIn().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden reservar fechas pasadas");
        }

        Alojamiento alojamiento = alojamientoRepo.findById(dto.getAlojamientoId())
                .orElseThrow(() -> new IllegalArgumentException("Alojamiento no existe"));
        Usuario usuario = usuarioRepo.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        // ⚠️ Ajusta este getter al nombre real de tu campo si no es 'capacidadMaxima'
        if (dto.getHuespedes() <= 0 || dto.getHuespedes() > alojamiento.getCapacidadMaxima()) {
            throw new IllegalArgumentException("Capacidad excedida");
        }

        boolean solapa = reservaRepo.existeSolapamiento(
                alojamiento.getId(), dto.getCheckIn(), dto.getCheckOut(), ESTADOS_ACTIVOS);
        if (solapa) {
            throw new IllegalStateException("Fechas no disponibles para este alojamiento");
        }

        Reserva r = mapper.map(dto, Reserva.class);
        r.setAlojamiento(alojamiento);
        r.setUsuario(usuario);

        long noches = Duration.between(dto.getCheckIn().atStartOfDay(), dto.getCheckOut().atStartOfDay()).toDays();
        BigDecimal total = alojamiento.getPrecioPorNoche().multiply(BigDecimal.valueOf(noches));
        r.setTotal(total);
        r.setEstado(EstadoReserva.PENDIENTE);

        r = reservaRepo.save(r);
        return mapper.map(r, ReservaDTO.class);
    }

    @Override
    public void cancelar(Long id) {
        var r = reservaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no existe"));

        if (r.getEstado() == EstadoReserva.CANCELADA || r.getEstado() == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("La reserva no puede cancelarse");
        }

        long horasRestantes = Duration.between(LocalDate.now().atStartOfDay(), r.getCheckIn().atStartOfDay()).toHours();
        if (horasRestantes < 48) {
            throw new IllegalStateException("Solo se puede cancelar hasta 48h antes del check-in");
        }

        r.setEstado(EstadoReserva.CANCELADA);
        reservaRepo.save(r);
    }

    @Override
    public Page<ReservaDTO>listarPorUsuario(Long usuarioId, Pageable pageable) {
        return reservaRepo.findByUsuario(usuarioId, pageable)
                .map(r -> mapper.map(r, ReservaDTO.class));
    }

    @Override
    public Page<ReservaDTO> listarPorAlojamiento(Long alojamientoId, Pageable pageable) {
        return reservaRepo.findByAlojamiento(alojamientoId, pageable)
                .map(r -> mapper.map(r, ReservaDTO.class));
    }
}
