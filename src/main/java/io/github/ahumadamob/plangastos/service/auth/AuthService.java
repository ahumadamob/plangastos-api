package io.github.ahumadamob.plangastos.service.auth;

import io.github.ahumadamob.plangastos.dto.auth.LoginRequestDto;
import io.github.ahumadamob.plangastos.dto.auth.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto request);
}
