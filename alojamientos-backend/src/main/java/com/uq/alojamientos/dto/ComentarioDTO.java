// ==========================================
// ComentarioDTO.java
// ==========================================
package com.uq.alojamientos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ComentarioDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "El ID de la reserva es obligatorio")
    @Positive(message = "El ID de la reserva debe ser positivo")
    private Long reservaId;

    @NotNull(message = "El ID del alojamiento es obligatorio")
    @Positive(message = "El ID del alojamiento debe ser positivo")
    private Long alojamientoId;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    private Long usuarioId;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario no puede estar vacío")
    @Size(min = 10, max = 500, message = "El comentario debe tener entre 10 y 500 caracteres")
    private String texto;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Size(max = 500, message = "La respuesta no puede exceder 500 caracteres")
    private String respuestaAnfitrion;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;
}