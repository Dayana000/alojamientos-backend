// domain/Reserva.java
package com.uq.alojamientos.domain;

import com.uq.alojamientos.domain.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "reservas",
        indexes = {
                @Index(name="idx_reserva_estado", columnList = "estado"),
                @Index(name="idx_reserva_aloj_usuario", columnList = "alojamiento_id,usuario_id")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reserva {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "alojamiento_id")
    private Alojamiento alojamiento;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private Integer huespedes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;
}
