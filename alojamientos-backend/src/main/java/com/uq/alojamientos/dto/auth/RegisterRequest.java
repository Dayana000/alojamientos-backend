package com.uq.alojamientos.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {

    @NotBlank
    @Size(min = 3, max = 100)
    private String nombre;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String telefono;

    @NotNull
    @Past
    private LocalDate fechaNacimiento;

    @NotBlank
    private String rol; // "USER" o "ANFITRION"

    @NotBlank
    @Size(min = 8)
    private String password;
}
