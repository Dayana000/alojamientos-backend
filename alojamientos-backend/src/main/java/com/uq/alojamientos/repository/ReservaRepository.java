package com.uq.alojamientos.repository;

import com.uq.alojamientos.domain.Reserva;
import com.uq.alojamientos.domain.enums.EstadoReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Set;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
        select count(r) > 0 from Reserva r
        where r.alojamiento.id = :alojamientoId
          and r.estado in :estadosActivos
          and r.checkIn  < :hasta
          and r.checkOut > :desde
        """)
    boolean existeSolapamiento(Long alojamientoId,
                               LocalDate desde,
                               LocalDate hasta,
                               Set<EstadoReserva> estadosActivos);

    @Query("select r from Reserva r where r.usuario.id = :usuarioId order by r.id desc")
    Page<Reserva> findByUsuario(Long usuarioId, Pageable pageable);

    @Query("select r from Reserva r where r.alojamiento.id = :alojamientoId order by r.id desc")
    Page<Reserva> findByAlojamiento(Long alojamientoId, Pageable pageable);
}
