package io.github.ahumadamob.plangastos.dto.auth;

import java.time.Instant;

public class AuthTokenResponseDto {

    private String accessToken;
    private String tokenType;
    private Instant expiresAt;

    public AuthTokenResponseDto(String accessToken, String tokenType, Instant expiresAt) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
