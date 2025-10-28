package com.curso.gameapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretBase64; // tem que ser Base64 no application-*.properties

    @Value("${jwt.expiration}")
    private long expirationMs; // milissegundos

    private SecretKey signingKey;

    @PostConstruct
    void initKey() {
        // converte a string Base64 em chave HMAC
        byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // ================= PUBLIC =================

    // gera um token pro usuário autenticado
    public String generateToken(UserDetails userDetails) {
        String role = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("USER");

        return buildToken(
                Map.of("role", role),
                userDetails.getUsername()
        );
    }

    // lê o "sub" (username) de dentro do token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // valida usuário + expiração
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // ================= HELPERS =================

    private String buildToken(Map<String, Object> extraClaims, String subject) {
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .claims(extraClaims)                          // nossas claims extras
                .subject(subject)                             // sub
                .issuedAt(new Date(now))                      // iat
                .expiration(new Date(now + expirationMs))     // exp
                .signWith(signingKey)                         // HS256
                .compact();
    }

    private boolean isTokenExpired(String token) {
        Date exp = extractClaim(token, Claims::getExpiration);
        return exp.before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // faz o parse + verificação da assinatura e devolve o corpo
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(signingKey)  // verifica assinatura com a mesma chave secreta
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
