package com.uq.alojamientos.domain;

import com.uq.alojamientos.domain.enums.EstadoAlojamiento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "alojamientos",
        indexes = {
                @Index(name = "idx_aloj_ciudad", columnList = "ciudad"),
                @Index(name = "idx_aloj_estado", columnList = "estado")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alojamiento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "anfitrion_id", nullable = false)
    private Usuario anfitrion;

    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false, length = 120)
    private String ciudad;

    @Column(length = 200)
    private String direccion;

    private Double latitud;
    private Double longitud;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioPorNoche;

    @Column(nullable = false)
    private Integer capacidadMaxima;

    /** CSV simple de servicios mientras no montes tabla aparte (wifi,piscina,...) */
    @Column(length = 500)
    private String serviciosCsv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoAlojamiento estado = EstadoAlojamiento.ACTIVO;
}
