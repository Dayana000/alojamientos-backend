// ==========================================
// UsuarioService.java (Interfaz actualizada)
// ==========================================
package com.uq.alojamientos.service;

import com.uq.alojamientos.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO registrar(UsuarioDTO dto);
    List<UsuarioDTO> listar();
    UsuarioDTO obtenerPorId(Long id);
}