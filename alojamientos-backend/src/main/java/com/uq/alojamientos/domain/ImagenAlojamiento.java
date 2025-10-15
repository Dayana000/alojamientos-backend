// domain/ImagenAlojamiento.java
package com.uq.alojamientos.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "imagenes_alojamiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ImagenAlojamiento {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "alojamiento_id")
    private Alojamiento alojamiento;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(nullable = false)
    private Boolean principal = false;
}
