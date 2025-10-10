package com.uq.alojamientos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.uq.alojamientos.model.Alojamiento;

public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {
  @Query("select a from Alojamiento a where a.estado <> 'ELIMINADO' and (:ciudad is null or a.ciudad = :ciudad)")
  Page<Alojamiento> findActivosByCiudad(@Param("ciudad") String ciudad, Pageable pageable);
}
