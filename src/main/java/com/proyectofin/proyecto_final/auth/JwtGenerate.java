package com.proyectofin.proyecto_final.auth;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.proyectofin.proyecto_final.enums.RoleName;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGenerate {
     
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Genera una clave secreta

    public String generateToken(Long userId, String email,List<RoleName> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId); // Añadir el userId como claim
        claims.put("roles", roles); // añadir los roles al token

        return Jwts.builder()
                .setClaims(claims) // Pasar el Map con los claims
                .setSubject(email) // Usuario al que pertenece el token
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // Expira en 1 hora
                .signWith(key) // Firma con la clave secreta
                .compact();
    }  

public Long extractUserId(String token) {
    Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
    return claims.get("id", Long.class);
}

}
