package com.uq.alojamientos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.uq.alojamientos.model.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
  Page<Comentario> findByAlojamientoIdOrderByFechaCreacionDesc(Long alojamientoId, Pageable pageable);
}
