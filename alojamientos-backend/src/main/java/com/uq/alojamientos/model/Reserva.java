package com.uq.alojamientos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.uq.alojamientos.model.enums.EstadoReserva;

@Entity
@Table(name = "reservas")
@Data @NoArgsConstructor @AllArgsConstructor
public class Reserva {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Usuario usuario;

  @ManyToOne(optional = false)
  private Alojamiento alojamiento;

  @Column(nullable = false)
  private LocalDate checkIn;

  @Column(nullable = false)
  private LocalDate checkOut;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private EstadoReserva estado = EstadoReserva.PENDIENTE;

  @Column(nullable = false)
  private Integer huespedes;
}
