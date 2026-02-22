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
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

    private final AuthProperties authProperties;

    public JwtTokenService(AuthProperties authProperties) {
        this.authProperties = authProperties;
        validateExpirationPolicy();
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
        JwtException lastException = null;
        for (SecretKey validationKey : getValidationKeys()) {
            try {
                return Jwts.parser()
                        .verifyWith(validationKey)
                        .build()
                        .parseSignedClaims(token);
            } catch (JwtException ex) {
                lastException = ex;
            }
        }

        if (lastException != null) {
            throw lastException;
        }
        throw new JwtException("No hay llaves de validación configuradas");
    }

    private SecretKey getSigningKey() {
        return buildSecretKey(authProperties.getJwt().getSecret());
    }

    private List<SecretKey> getValidationKeys() {
        List<String> validationSecrets = authProperties.getJwt().getValidationSecrets();
        if (validationSecrets == null || validationSecrets.isEmpty()) {
            return List.of(getSigningKey());
        }

        return validationSecrets.stream()
                .filter(secret -> secret != null && !secret.isBlank())
                .map(this::buildSecretKey)
                .toList();
    }

    private SecretKey buildSecretKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private void validateExpirationPolicy() {
        long expiration = authProperties.getJwt().getExpirationSeconds();
        long minExpiration = authProperties.getJwt().getMinExpirationSeconds();
        long maxExpiration = authProperties.getJwt().getMaxExpirationSeconds();

        if (expiration < minExpiration || expiration > maxExpiration) {
            throw new IllegalStateException(
                    "plangastos.auth.jwt.expiration-seconds fuera de la política permitida ["
                    + minExpiration + ", " + maxExpiration + "]");
        }
    }

    public record JwtTokenResult(String token, Instant expiresAt) {
    }
}
