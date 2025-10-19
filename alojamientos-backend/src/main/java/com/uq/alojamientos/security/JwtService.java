package com.uq.alojamientos.security;

import com.uq.alojamientos.domain.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio JWT compatible con jjwt 0.12.x
 */
@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMillis;

    public JwtService(
            @Value("${app.security.jwt-secret:${app.jwt.secret:change_me}}") String secretHeader,
            @Value("${app.jwt.expiration-minutes:120}") long expirationMinutes
    ) {
        byte[] keyBytes = secretHeader.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMillis = expirationMinutes * 60_000L;
    }

    public String generate(String subject, Map<String, Object> claims) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date exp = Date.from(now.plusMillis(expirationMillis));
        Map<String, Object> safeClaims = claims != null ? claims : new HashMap<>();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(exp)
                .addClaims(safeClaims)
                .signWith(key)
                .compact();
    }

    public String generate(Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        if (usuario != null) {
            if (usuario.getEmail() != null) claims.put("email", usuario.getEmail());
            if (usuario.getRol() != null) claims.put("roles", usuario.getRol().name());
            if (usuario.getNombre() != null) claims.put("nombre", usuario.getNombre());
        }
        String subject = (usuario != null && usuario.getEmail() != null) ? usuario.getEmail() : "anonymous";
        return generate(subject, claims);
    }

    public Jws<Claims> parse(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public String getSubject(String token) {
        try {
            Jws<Claims> jws = parse(token);
            return jws.getBody().getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

    public Claims getClaims(String token) {
        try {
            Jws<Claims> jws = parse(token);
            return jws.getBody();
        } catch (Exception ex) {
            return null;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            parse(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (SignatureException | MalformedJwtException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
