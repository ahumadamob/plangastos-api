package io.github.ahumadamob.plangastos.auth;

import io.github.ahumadamob.plangastos.config.AuthProperties;
import io.github.ahumadamob.plangastos.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final AuthProperties authProperties;

    public JwtTokenService(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public JwtTokenResult createAccessToken(Usuario usuario) {
        Instant issuedAt = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiresAt = issuedAt.plusSeconds(authProperties.getJwt().getExpirationSeconds());

        String token = Jwts.builder()
                .subject(String.valueOf(usuario.getId()))
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .signWith(getSigningKey())
                .compact();

        return new JwtTokenResult(token, expiresAt);
    }

    public Long extractSubjectAsUserId(String token) {
        String sub = parseSignedClaims(token).getPayload().getSubject();
        if (sub == null || sub.isBlank()) {
            throw new JwtException("Token sin subject");
        }
        try {
            return Long.parseLong(sub);
        } catch (NumberFormatException ex) {
            throw new JwtException("Subject inválido", ex);
        }
    }

    private Jws<Claims> parseSignedClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(authProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public record JwtTokenResult(String token, Instant expiresAt) {
    }
}
