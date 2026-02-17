package io.github.ahumadamob.plangastos.security;

import io.github.ahumadamob.plangastos.auth.CurrentUser;
import io.github.ahumadamob.plangastos.auth.JwtAuthenticationToken;
import io.github.ahumadamob.plangastos.auth.JwtTokenService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(JwtTokenService jwtTokenService,
            RestAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtTokenService = jwtTokenService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            if (header != null && !header.isBlank()) {
                if (!header.startsWith(BEARER_PREFIX)) {
                    throw new JwtException("Esquema de autorización inválido");
                }

                String token = header.substring(BEARER_PREFIX.length()).trim();
                if (token.isBlank()) {
                    throw new JwtException("Token vacío");
                }

                Long userId = jwtTokenService.extractSubjectAsUserId(token);
                CurrentUser currentUser = new CurrentUser(userId);
                request.setAttribute("currentUser", currentUser);
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(currentUser));
            }

            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            SecurityContextHolder.clearContext();
            authenticationEntryPoint.commence(request, response,
                    new org.springframework.security.authentication.BadCredentialsException("Token inválido", ex));
        }
    }
}
