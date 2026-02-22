package io.github.ahumadamob.plangastos.auth;

import io.github.ahumadamob.plangastos.dto.auth.AuthLoginRequestDto;
import io.github.ahumadamob.plangastos.dto.auth.AuthRegisterRequestDto;
import io.github.ahumadamob.plangastos.dto.auth.AuthTokenResponseDto;
import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.exception.AuthenticationException;
import io.github.ahumadamob.plangastos.exception.BusinessValidationException;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final LoginRateLimiterService loginRateLimiterService;
    private final AuthEventLogger authEventLogger;

    public AuthService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenService jwtTokenService,
            LoginRateLimiterService loginRateLimiterService,
            AuthEventLogger authEventLogger) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
        this.loginRateLimiterService = loginRateLimiterService;
        this.authEventLogger = authEventLogger;
    }

    public AuthTokenResponseDto login(AuthLoginRequestDto request, HttpServletRequest httpRequest) {
        String email = request.getEmail();
        String password = request.getPassword();
        boolean blankPassword = password == null || password.isBlank();

        if (!blankPassword && loginRateLimiterService.isBlocked(email)) {
            authEventLogger.loginFailure(email, "rate_limited", httpRequest);
            throw new AuthenticationException("Demasiados intentos fallidos. Intenta más tarde");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> failAuth(email, "invalid_credentials", httpRequest));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw failAuth(email, "inactive_user", httpRequest);
        }

        if (!blankPassword && !passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw failAuth(email, "invalid_credentials", httpRequest);
        }

        loginRateLimiterService.reset(email);
        authEventLogger.loginSuccess(email, httpRequest);

        JwtTokenService.JwtTokenResult tokenResult = jwtTokenService.createAccessToken(usuario);
        return new AuthTokenResponseDto(tokenResult.token(), "Bearer", tokenResult.expiresAt());
    }

    public AuthTokenResponseDto register(AuthRegisterRequestDto request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessValidationException("Ya existe un usuario registrado con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setActivo(true);

        Usuario saved = usuarioRepository.save(usuario);
        JwtTokenService.JwtTokenResult tokenResult = jwtTokenService.createAccessToken(saved);
        return new AuthTokenResponseDto(tokenResult.token(), "Bearer", tokenResult.expiresAt());
    }

    private AuthenticationException failAuth(String email, String reason, HttpServletRequest httpRequest) {
        loginRateLimiterService.registerFailure(email);
        authEventLogger.loginFailure(email, reason, httpRequest);
        return new AuthenticationException("Credenciales inválidas");
    }
}
