package io.github.ahumadamob.plangastos.config;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "plangastos.auth")
public class AuthProperties {

    private final Jwt jwt = new Jwt();
    private final LoginRateLimit loginRateLimit = new LoginRateLimit();

    public Jwt getJwt() {
        return jwt;
    }

    public LoginRateLimit getLoginRateLimit() {
        return loginRateLimit;
    }

    public static class Jwt {

        private String secret;
        private List<String> validationSecrets = new ArrayList<>();
        private long expirationSeconds;
        private long minExpirationSeconds = 300;
        private long maxExpirationSeconds = 7200;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public List<String> getValidationSecrets() {
            return validationSecrets;
        }

        public void setValidationSecrets(List<String> validationSecrets) {
            this.validationSecrets = validationSecrets;
        }

        public long getExpirationSeconds() {
            return expirationSeconds;
        }

        public void setExpirationSeconds(long expirationSeconds) {
            this.expirationSeconds = expirationSeconds;
        }

        public long getMinExpirationSeconds() {
            return minExpirationSeconds;
        }

        public void setMinExpirationSeconds(long minExpirationSeconds) {
            this.minExpirationSeconds = minExpirationSeconds;
        }

        public long getMaxExpirationSeconds() {
            return maxExpirationSeconds;
        }

        public void setMaxExpirationSeconds(long maxExpirationSeconds) {
            this.maxExpirationSeconds = maxExpirationSeconds;
        }
    }

    public static class LoginRateLimit {

        private boolean enabled = true;
        private int maxAttempts = 5;
        private long windowSeconds = 300;
        private long blockSeconds = 600;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getMaxAttempts() {
            return maxAttempts;
        }

        public void setMaxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        public long getWindowSeconds() {
            return windowSeconds;
        }

        public void setWindowSeconds(long windowSeconds) {
            this.windowSeconds = windowSeconds;
        }

        public long getBlockSeconds() {
            return blockSeconds;
        }

        public void setBlockSeconds(long blockSeconds) {
            this.blockSeconds = blockSeconds;
        }
    }
}
