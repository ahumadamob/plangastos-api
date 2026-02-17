package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.auth.AuthService;
import io.github.ahumadamob.plangastos.dto.auth.AuthLoginRequestDto;
import io.github.ahumadamob.plangastos.dto.auth.AuthRegisterRequestDto;
import io.github.ahumadamob.plangastos.dto.auth.AuthTokenResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth")
@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseSuccessDto<AuthTokenResponseDto>> login(@Valid @RequestBody AuthLoginRequestDto request) {
        return ResponseEntity.ok(ApiResponseFactory.success(authService.login(request), "Login exitoso"));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseSuccessDto<AuthTokenResponseDto>> register(@Valid @RequestBody AuthRegisterRequestDto request) {
        return ResponseEntity.ok(ApiResponseFactory.success(authService.register(request), "Usuario registrado"));
    }
}
