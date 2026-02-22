package io.github.ahumadamob.plangastos.auth;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthEventLogger {

    private static final Logger log = LoggerFactory.getLogger(AuthEventLogger.class);

    public void loginSuccess(String email, HttpServletRequest request) {
        log.info("auth_event=LOGIN_SUCCESS principal_hash={} ip={} user_agent={}",
                hashPrincipal(email),
                resolveClientIp(request),
                sanitize(request != null ? request.getHeader("User-Agent") : null));
    }

    public void loginFailure(String email, String reason, HttpServletRequest request) {
        log.warn("auth_event=LOGIN_FAILURE principal_hash={} reason={} ip={} user_agent={}",
                hashPrincipal(email),
                sanitize(reason),
                resolveClientIp(request),
                sanitize(request != null ? request.getHeader("User-Agent") : null));
    }

    private String resolveClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return sanitize(forwardedFor.split(",")[0]);
        }
        return sanitize(request.getRemoteAddr());
    }

    private String hashPrincipal(String principal) {
        String value = principal == null ? "unknown" : principal.trim().toLowerCase();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            return toHex(hash).substring(0, 12);
        } catch (NoSuchAlgorithmException ex) {
            return "hash-error";
        }
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String sanitize(String value) {
        if (value == null || value.isBlank()) {
            return "unknown";
        }
        return value.replaceAll("[\\r\\n\\t]", "_");
    }
}
