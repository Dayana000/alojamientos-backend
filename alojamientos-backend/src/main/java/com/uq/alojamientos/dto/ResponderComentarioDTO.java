// ==========================================
// ResponderComentarioDTO.java
// ==========================================
package com.uq.alojamientos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResponderComentarioDTO {

    @NotBlank(message = "La respuesta no puede estar vacía")
    @Size(min = 10, max = 500, message = "La respuesta debe tener entre 10 y 500 caracteres")
    private String respuesta;
}