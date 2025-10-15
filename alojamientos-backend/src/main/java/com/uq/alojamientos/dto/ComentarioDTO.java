package com.uq.alojamientos.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComentarioDTO {
    private Long id;
    private Long reservaId;
    private Long alojamientoId;
    private Long usuarioId;
    private Integer calificacion;        // 1..5
    private String texto;                // máx 500 chars (valídalo en controller si deseas)
    private String respuestaAnfitrion;   // puede ser null
    private LocalDateTime createdAt;     // se setea en @PrePersist en la entidad
}
