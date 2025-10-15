package com.uq.alojamientos.security;

import com.uq.alojamientos.domain.Usuario;
import com.uq.alojamientos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario u = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Asumimos enum RolUsuario { ADMIN, ANFITRION, USER }
        String role = "ROLE_" + u.getRol().name();
        return new User(u.getEmail(), u.getPasswordHash(), List.of(new SimpleGrantedAuthority(role)));
    }
}
