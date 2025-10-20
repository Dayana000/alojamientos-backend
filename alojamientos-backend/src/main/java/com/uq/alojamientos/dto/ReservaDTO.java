// ==========================================
// ReservaDTO.java
// ==========================================
package com.uq.alojamientos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.uq.alojamientos.domain.enums.EstadoReserva;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReservaDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "El ID del usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    private Long usuarioId;

    @NotNull(message = "El ID del alojamiento es obligatorio")
    @Positive(message = "El ID del alojamiento debe ser positivo")
    private Long alojamientoId;

    @NotNull(message = "La fecha de check-in es obligatoria")
    @FutureOrPresent(message = "La fecha de check-in debe ser presente o futura")
    private LocalDate checkIn;

    @NotNull(message = "La fecha de check-out es obligatoria")
    @Future(message = "La fecha de check-out debe ser futura")
    private LocalDate checkOut;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private EstadoReserva estado;

    @NotNull(message = "El número de huéspedes es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 huésped")
    @Max(value = 20, message = "No se permiten más de 20 huéspedes")
    private Integer huespedes;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Digits(integer = 10, fraction = 2, message = "Formato de total inválido")
    private BigDecimal total;
}