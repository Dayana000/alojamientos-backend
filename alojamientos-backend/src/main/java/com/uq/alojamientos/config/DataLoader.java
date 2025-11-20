package com.uq.alojamientos.config;

import com.uq.alojamientos.domain.Alojamiento;
import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.domain.enums.EstadoAlojamiento;
import com.uq.alojamientos.repository.AlojamientoRepository;
import com.uq.alojamientos.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final AlojamientoRepository alojamientoRepository;
    private final UsuarioRepository usuarioRepository;

    @PostConstruct
    @Transactional
    public void init() {

        // Comprobar cuántos alojamientos hay ya
        long count = alojamientoRepository.count();

        // Si ya tenemos 24 o más alojamientos, asumimos que los datos de prueba ya existen
        if (count >= 24) {
            System.out.println("DataLoader: ya hay " + count + " alojamientos, no se insertan datos de prueba.");
            return;
        }

        // Busca cualquier usuario existente para usarlo como anfitrión
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            System.out.println("DataLoader: No hay usuarios registrados. No se cargan alojamientos.");
            return;
        }

        Usuario anfitrion = usuarios.get(0); // primer usuario como anfitrión de prueba

        List<Alojamiento> alojamientos = new ArrayList<>();

        // ======================================
        //  MANIZALES – 8 alojamientos
        // ======================================
        alojamientos.add(crearAlojamiento("Cabaña en el bosque", "Hermosa cabaña rústica.", "Manizales",
                "Vereda El Manantial", 5.07, -75.51, 180000, 4, anfitrion));

        alojamientos.add(crearAlojamiento("Apartamento moderno centro", "Apartamento con vista panorámica.", "Manizales",
                "Cra 23 #45-10", 5.071, -75.515, 230000, 3, anfitrion));

        alojamientos.add(crearAlojamiento("Finca cafetera con piscina", "Piscina y zonas verdes.", "Manizales",
                "Km 8 vía Magdalena", 5.075, -75.52, 350000, 8, anfitrion));

        alojamientos.add(crearAlojamiento("Loft estudio", "Loft ideal para parejas.", "Manizales",
                "Calle 50 #20-10", 5.06, -75.53, 190000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Suite premium", "Suite de lujo equipada.", "Manizales",
                "Av Santander #30-25", 5.08, -75.52, 400000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Habitación económica", "Habitación básica y económica.", "Manizales",
                "Barrio La Francia", 5.065, -75.505, 100000, 1, anfitrion));

        alojamientos.add(crearAlojamiento("Casa familiar", "Casa amplia para grupos.", "Manizales",
                "La Enea", 5.07, -75.49, 320000, 7, anfitrion));

        alojamientos.add(crearAlojamiento("Penthouse vista ciudad", "Penthouse con terraza.", "Manizales",
                "Los Alcázares", 5.075, -75.515, 500000, 5, anfitrion));


        // ======================================
        //  PEREIRA – 8 alojamientos
        // ======================================
        alojamientos.add(crearAlojamiento("Loft cerca del aeropuerto", "Loft cómodo y moderno.", "Pereira",
                "Matecaña", 4.815, -75.74, 200000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Casa campestre jacuzzi", "Jacuzzi privado.", "Pereira",
                "La Florida", 4.82, -75.75, 420000, 6, anfitrion));

        alojamientos.add(crearAlojamiento("Habitación centro", "Habitación sencilla.", "Pereira",
                "Calle 20 #7-15", 4.813, -75.695, 120000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Villa de lujo", "Casa con piscina privada.", "Pereira",
                "Tribunas", 4.812, -75.735, 650000, 10, anfitrion));

        alojamientos.add(crearAlojamiento("Apartamento norte", "Apartamento moderno.", "Pereira",
                "Pinares", 4.814, -75.702, 270000, 3, anfitrion));

        alojamientos.add(crearAlojamiento("Hostal económico", "Hostal para mochileros.", "Pereira",
                "Centro", 4.815, -75.69, 80000, 1, anfitrion));

        alojamientos.add(crearAlojamiento("Suite ejecutiva", "Suite para negocios.", "Pereira",
                "Circunvalar", 4.811, -75.71, 350000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Casa rústica", "Casa estilo rústico campestre.", "Pereira",
                "Combia", 4.82, -75.76, 280000, 5, anfitrion));


        // ======================================
        //  ARMENIA – 8 alojamientos
        // ======================================
        alojamientos.add(crearAlojamiento("Glamping montañas", "Glamping con vista.", "Armenia",
                "La Virginia", 4.535, -75.67, 260000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Apartamento familiar", "Ideal para familias.", "Armenia",
                "Av Bolívar", 4.54, -75.66, 230000, 5, anfitrion));

        alojamientos.add(crearAlojamiento("Finca ecológica", "Senderos y miradores.", "Armenia",
                "Km 4 vía al Valle", 4.55, -75.68, 380000, 10, anfitrion));

        alojamientos.add(crearAlojamiento("Habitación privada", "Habitación cómoda.", "Armenia",
                "Centro", 4.535, -75.65, 110000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Casa colonial", "Casa estilo colonial.", "Armenia",
                "Los Naranjos", 4.543, -75.663, 300000, 6, anfitrion));

        alojamientos.add(crearAlojamiento("Cabaña romántica", "Cabaña ideal parejas.", "Armenia",
                "El Caimo", 4.54, -75.68, 250000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Estudio moderno", "Estudio equipado.", "Armenia",
                "Norte", 4.55, -75.66, 200000, 2, anfitrion));

        alojamientos.add(crearAlojamiento("Casa con BBQ", "Terraza con BBQ.", "Armenia",
                "Calima", 4.545, -75.67, 320000, 6, anfitrion));


        alojamientoRepository.saveAll(alojamientos);
    }

    private Alojamiento crearAlojamiento(
            String titulo,
            String descripcion,
            String ciudad,
            String direccion,
            Double latitud,
            Double longitud,
            Integer precio,
            Integer capacidad,
            Usuario anfitrion
    ) {
        Alojamiento a = new Alojamiento();
        a.setTitulo(titulo);
        a.setDescripcion(descripcion);
        a.setCiudad(ciudad);
        a.setDireccion(direccion);
        a.setLatitud(latitud);
        a.setLongitud(longitud);
        a.setPrecioPorNoche(new BigDecimal(precio));
        a.setCapacidadMaxima(capacidad);
        a.setAnfitrion(anfitrion);
        a.setEstado(EstadoAlojamiento.ACTIVO);
        a.setServiciosLista(List.of("wifi", "cocina"));
        return a;
    }
}
