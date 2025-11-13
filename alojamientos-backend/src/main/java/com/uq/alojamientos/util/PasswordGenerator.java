package com.uq.alojamientos.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "12345678"; // cambia aquí si quieres otra clave
        String hash = encoder.encode(rawPassword);

        System.out.println("\nHash generado para 12345678:");
        System.out.println(hash);
    }
}
