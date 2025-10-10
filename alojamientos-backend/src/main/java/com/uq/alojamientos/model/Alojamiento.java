package com.uq.alojamientos.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "alojamientos")
@Data @NoArgsConstructor @AllArgsConstructor
public class Alojamiento {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Usuario anfitrion;

  @Column(nullable = false)
  private String titulo;

  @Column(length = 2000)
  private String descripcion;

  @Column(nullable = false)
  private String ciudad;

  private String direccion;
  private Double latitud;
  private Double longitud;

  @Column(nullable = false)
  private BigDecimal precioPorNoche;

  @Column(nullable = false)
  private Integer capacidadMaxima;

  @Column(nullable = false)
  private String estado; // ACTIVO | ELIMINADO (soft delete)
}
