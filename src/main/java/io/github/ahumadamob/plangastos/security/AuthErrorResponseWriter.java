package io.github.ahumadamob.plangastos.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ahumadamob.plangastos.dto.common.ErrorDetailDto;
import io.github.ahumadamob.plangastos.dto.common.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class AuthErrorResponseWriter {

    private final ObjectMapper objectMapper;

    public AuthErrorResponseWriter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void write(HttpServletRequest request, HttpServletResponse response, int status, String error,
            String errorCode, String message) throws IOException {
        ErrorResponseDto body = new ErrorResponseDto(
                LocalDateTime.now(),
                status,
                error,
                List.of(new ErrorDetailDto("auth", message)),
                errorCode,
                request.getRequestURI());

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
