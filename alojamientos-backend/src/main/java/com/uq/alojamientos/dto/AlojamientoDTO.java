// ==========================================
// AlojamientoDTO.java
// ==========================================
package com.uq.alojamientos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AlojamientoDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull(message = "El ID del anfitrión es obligatorio")
    @Positive(message = "El ID del anfitrión debe ser positivo")
    private Long anfitrionId;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 5, max = 120, message = "El título debe tener entre 5 y 120 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 20, max = 1000, message = "La descripción debe tener entre 20 y 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 120, message = "La ciudad no puede exceder 120 caracteres")
    private String ciudad;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    private String direccion;

    @DecimalMin(value = "-90.0", message = "Latitud inválida (debe estar entre -90 y 90)")
    @DecimalMax(value = "90.0", message = "Latitud inválida (debe estar entre -90 y 90)")
    private Double latitud;

    @DecimalMin(value = "-180.0", message = "Longitud inválida (debe estar entre -180 y 180)")
    @DecimalMax(value = "180.0", message = "Longitud inválida (debe estar entre -180 y 180)")
    private Double longitud;

    @NotNull(message = "El precio por noche es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "Formato de precio inválido")
    private BigDecimal precioPorNoche;

    @NotNull(message = "La capacidad máxima es obligatoria")
    @Min(value = 1, message = "La capacidad mínima es 1 persona")
    @Max(value = 50, message = "La capacidad máxima es 50 personas")
    private Integer capacidadMaxima;

    @Size(max = 10, message = "Máximo 10 servicios permitidos")
    private List<@NotBlank(message = "El servicio no puede estar vacío") String> servicios;
}
