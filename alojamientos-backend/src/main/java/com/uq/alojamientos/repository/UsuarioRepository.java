package com.uq.alojamientos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uq.alojamientos.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
  Optional<Usuario> findByEmail(String email);
}
