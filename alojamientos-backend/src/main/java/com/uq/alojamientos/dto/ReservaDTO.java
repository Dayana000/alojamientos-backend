package com.uq.alojamientos.dto;

import lombok.Data;
import java.time.LocalDate;
import com.uq.alojamientos.model.enums.EstadoReserva;

@Data
public class ReservaDTO {
  private Long id;
  private Long usuarioId;
  private Long alojamientoId;
  private LocalDate checkIn;
  private LocalDate checkOut;
  private EstadoReserva estado;
  private Integer huespedes;
}
