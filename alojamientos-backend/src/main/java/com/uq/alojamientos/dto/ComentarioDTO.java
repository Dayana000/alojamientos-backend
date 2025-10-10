package com.uq.alojamientos.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioDTO {
  private Long id;
  private Long usuarioId;
  private Long alojamientoId;
  private Integer calificacion;
  private String texto;
  private LocalDateTime fechaCreacion;
}
