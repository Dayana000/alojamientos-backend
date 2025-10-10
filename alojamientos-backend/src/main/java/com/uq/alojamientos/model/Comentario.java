package com.uq.alojamientos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentarios")
@Data @NoArgsConstructor @AllArgsConstructor
public class Comentario {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Usuario usuario;

  @ManyToOne(optional = false)
  private Alojamiento alojamiento;

  @Column(nullable = false)
  private Integer calificacion; // 1..5

  @Column(length = 500)
  private String texto;

  @Column(nullable = false)
  private LocalDateTime fechaCreacion = LocalDateTime.now();
}
