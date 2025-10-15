package com.uq.alojamientos.repository;

import com.uq.alojamientos.domain.Alojamiento;
import com.uq.alojamientos.domain.enums.EstadoAlojamiento;
import com.uq.alojamientos.domain.enums.EstadoReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

    Page<Alojamiento> findByEstadoAndCiudadContainingIgnoreCase(
            EstadoAlojamiento estado, String ciudad, Pageable pageable);

    @Query("""
      select a from Alojamiento a
      where a.estado = com.uq.alojamientos.domain.enums.EstadoAlojamiento.ACTIVO
        and (:ciudad is null or lower(a.ciudad) like lower(concat('%', :ciudad, '%')))
        and (:precioMin is null or a.precioPorNoche >= :precioMin)
        and (:precioMax is null or a.precioPorNoche <= :precioMax)
        and (:capacidad is null or a.capacidadMaxima >= :capacidad)
        and not exists (
            select r.id from Reserva r
            where r.alojamiento = a
              and r.estado in :estadosActivos
              and r.checkIn  < :hasta
              and r.checkOut > :desde
        )
      """)
    Page<Alojamiento> buscarDisponibles(
            String ciudad,
            BigDecimal precioMin,
            BigDecimal precioMax,
            Integer capacidad,
            LocalDate desde,
            LocalDate hasta,
            Set<EstadoReserva> estadosActivos,
            Pageable pageable
    );
}
