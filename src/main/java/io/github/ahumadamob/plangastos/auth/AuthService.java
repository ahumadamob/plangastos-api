package io.github.ahumadamob.plangastos.auth;

import io.github.ahumadamob.plangastos.dto.auth.AuthLoginRequestDto;
import io.github.ahumadamob.plangastos.dto.auth.AuthRegisterRequestDto;
import io.github.ahumadamob.plangastos.dto.auth.AuthTokenResponseDto;
import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.exception.AuthenticationException;
import io.github.ahumadamob.plangastos.exception.BusinessValidationException;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    public AuthTokenResponseDto login(AuthLoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Credenciales inválidas"));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new AuthenticationException("El usuario está inactivo");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new AuthenticationException("Credenciales inválidas");
        }

        JwtTokenService.JwtTokenResult tokenResult = jwtTokenService.createAccessToken(usuario);
        return new AuthTokenResponseDto(tokenResult.token(), "Bearer", tokenResult.expiresAt());
    }

    public AuthTokenResponseDto register(AuthRegisterRequestDto request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BusinessValidationException("Ya existe un usuario registrado con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setActivo(true);

        Usuario saved = usuarioRepository.save(usuario);
        JwtTokenService.JwtTokenResult tokenResult = jwtTokenService.createAccessToken(saved);
        return new AuthTokenResponseDto(tokenResult.token(), "Bearer", tokenResult.expiresAt());
    }
}
