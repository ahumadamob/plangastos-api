package io.github.ahumadamob.plangastos.auth;

import io.github.ahumadamob.plangastos.config.AuthProperties;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LoginRateLimiterService {

    private final AuthProperties authProperties;
    private final Map<String, AttemptWindow> windowsByPrincipal = new ConcurrentHashMap<>();

    public LoginRateLimiterService(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public boolean isBlocked(String principal) {
        if (!authProperties.getLoginRateLimit().isEnabled()) {
            return false;
        }

        AttemptWindow attemptWindow = windowsByPrincipal.get(normalizePrincipal(principal));
        if (attemptWindow == null) {
            return false;
        }

        Instant now = Instant.now();
        if (attemptWindow.blockedUntil != null && now.isBefore(attemptWindow.blockedUntil)) {
            return true;
        }

        if (attemptWindow.blockedUntil != null && !now.isBefore(attemptWindow.blockedUntil)) {
            windowsByPrincipal.remove(normalizePrincipal(principal));
        }
        return false;
    }

    public void registerFailure(String principal) {
        if (!authProperties.getLoginRateLimit().isEnabled()) {
            return;
        }

        String normalized = normalizePrincipal(principal);
        Instant now = Instant.now();
        long windowSeconds = authProperties.getLoginRateLimit().getWindowSeconds();
        long blockSeconds = authProperties.getLoginRateLimit().getBlockSeconds();
        int maxAttempts = authProperties.getLoginRateLimit().getMaxAttempts();

        windowsByPrincipal.compute(normalized, (key, current) -> {
            if (current == null || current.windowStartedAt.plusSeconds(windowSeconds).isBefore(now)) {
                current = new AttemptWindow(now, 0, null);
            }

            int nextAttempts = current.attempts + 1;
            Instant blockedUntil = current.blockedUntil;
            if (nextAttempts >= maxAttempts) {
                blockedUntil = now.plusSeconds(blockSeconds);
            }
            return new AttemptWindow(current.windowStartedAt, nextAttempts, blockedUntil);
        });
    }

    public void reset(String principal) {
        windowsByPrincipal.remove(normalizePrincipal(principal));
    }

    private String normalizePrincipal(String principal) {
        return principal == null ? "unknown" : principal.trim().toLowerCase();
    }

    private record AttemptWindow(Instant windowStartedAt, int attempts, Instant blockedUntil) {
    }
}
