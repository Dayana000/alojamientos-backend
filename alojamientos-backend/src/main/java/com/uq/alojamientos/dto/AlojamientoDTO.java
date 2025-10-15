package com.uq.alojamientos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AlojamientoDTO {
    private Long id;
    private Long anfitrionId;
    private String titulo;
    private String descripcion;
    private String ciudad;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private BigDecimal precioPorNoche;
    private Integer capacidadMaxima;
    /** opcional: lista de servicios; si no la tienes, borra el mapeo en el service */
    private java.util.List<String> servicios;
}
