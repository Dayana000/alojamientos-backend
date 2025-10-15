package com.uq.alojamientos.repository;

import com.uq.alojamientos.domain.Comentario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    boolean existsByReservaId(Long reservaId);

    Page<Comentario> findByAlojamientoIdOrderByCreatedAtDesc(Long alojamientoId, Pageable pageable);

    @Query("select avg(c.calificacion) from Comentario c where c.alojamiento.id = :alojamientoId")
    Double promedioPorAlojamiento(Long alojamientoId);
}
