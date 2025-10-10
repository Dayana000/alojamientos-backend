package com.uq.alojamientos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uq.alojamientos.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
