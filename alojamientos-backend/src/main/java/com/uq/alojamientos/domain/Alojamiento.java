package com.uq.alojamientos.domain;

import com.uq.alojamientos.domain.enums.EstadoAlojamiento;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "alojamientos",
        indexes = {
                @Index(name = "idx_aloj_ciudad", columnList = "ciudad"),
                @Index(name = "idx_aloj_estado", columnList = "estado"),
                @Index(name = "idx_aloj_anfitrion", columnList = "anfitrion_id")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alojamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /**
     * CSV simple de servicios mientras no montes tabla aparte
     * Ejemplo: "wifi,piscina,estacionamiento,aire_acondicionado"
     */
    @Column(length = 500)
    private String serviciosCsv;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EstadoAlojamiento estado = EstadoAlojamiento.ACTIVO;

    // Relación bidireccional con imágenes (opcional pero útil)
    @OneToMany(mappedBy = "alojamiento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ImagenAlojamiento> imagenes = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // ========== MÉTODOS DE UTILIDAD ==========

    /**
     * Convierte el CSV de servicios en lista
     * ✅ ESTE ES EL MÉTODO QUE FALTABA
     */
    public List<String> getServiciosLista() {
        if (serviciosCsv == null || serviciosCsv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(serviciosCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * Setea servicios desde lista
     */
    public void setServiciosLista(List<String> servicios) {
        if (servicios == null || servicios.isEmpty()) {
            this.serviciosCsv = null;
        } else {
            this.serviciosCsv = String.join(",", servicios);
        }
    }

    /**
     * Agrega una imagen a la lista
     */
    public void agregarImagen(ImagenAlojamiento imagen) {
        imagenes.add(imagen);
        imagen.setAlojamiento(this);
    }

    /**
     * Remueve una imagen de la lista
     */
    public void removerImagen(ImagenAlojamiento imagen) {
        imagenes.remove(imagen);
        imagen.setAlojamiento(null);
    }

    // ========== CALLBACKS JPA ==========

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoAlojamiento.ACTIVO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}