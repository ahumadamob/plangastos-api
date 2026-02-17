package io.github.ahumadamob.plangastos.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.ahumadamob.plangastos.config.AuthProperties;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.repository.PresupuestoRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthAndOwnershipIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthProperties authProperties;

    @BeforeEach
    void setUp() {
        presupuestoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void login_CredencialesValidas_DebeRetornarToken() throws Exception {
        crearUsuario("valido@test.com", "Password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "valido@test.com",
                                  "password": "Password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.expiresAt").isNotEmpty());
    }

    @Test
    void login_CredencialesInvalidas_DebeRetornarUnauthorized() throws Exception {
        crearUsuario("invalido@test.com", "Password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "invalido@test.com",
                                  "password": "PasswordIncorrecto"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    void listadoPresupuestos_DebeMostrarSoloRecursosDelUsuarioAutenticado() throws Exception {
        Usuario usuarioA = crearUsuario("usuario.a@test.com", "Password123");
        Usuario usuarioB = crearUsuario("usuario.b@test.com", "Password123");

        Presupuesto presupuestoA = crearPresupuesto(usuarioA, "Presupuesto A");
        crearPresupuesto(usuarioB, "Presupuesto B");

        String tokenUsuarioA = loginYObtenerToken("usuario.a@test.com", "Password123");

        mockMvc.perform(get("/api/v1/presupuesto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUsuarioA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].id").value(presupuestoA.getId()))
                .andExpect(jsonPath("$.data[0].nombre").value("Presupuesto A"));
    }

    @Test
    void accesoPorId_RecursoDeOtroUsuario_DebeRetornarNotFound() throws Exception {
        crearUsuario("owner@test.com", "Password123");
        Usuario usuarioB = crearUsuario("other@test.com", "Password123");

        Presupuesto presupuestoDeB = crearPresupuesto(usuarioB, "Privado B");
        String tokenUsuarioA = loginYObtenerToken("owner@test.com", "Password123");

        mockMvc.perform(get("/api/v1/presupuesto/{id}", presupuestoDeB.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenUsuarioA))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("NOT_FOUND"));
    }

    @Test
    void endpointsProtegidos_SinToken_DebeRetornarUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/presupuesto"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    void endpointsProtegidos_TokenInvalido_DebeRetornarUnauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/presupuesto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token.invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    @Test
    void endpointsProtegidos_TokenVencido_DebeRetornarUnauthorized() throws Exception {
        Usuario usuario = crearUsuario("expira@test.com", "Password123");
        String tokenVencido = crearTokenVencido(usuario.getId());

        mockMvc.perform(get("/api/v1/presupuesto")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenVencido))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value("UNAUTHORIZED"));
    }

    private Usuario crearUsuario(String email, String plainPassword) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordEncoder.encode(plainPassword));
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    private Presupuesto crearPresupuesto(Usuario usuario, String nombre) {
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setUsuario(usuario);
        presupuesto.setNombre(nombre);
        presupuesto.setFechaDesde(LocalDate.of(2025, 1, 1));
        presupuesto.setFechaHasta(LocalDate.of(2025, 1, 31));
        presupuesto.setInactivo(false);
        return presupuestoRepository.save(presupuesto);
    }

    private String loginYObtenerToken(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "%s",
                                  "password": "%s"
                                }
                                """.formatted(email, password)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        return root.path("data").path("token").asText();
    }

    private String crearTokenVencido(Long usuarioId) {
        Instant issuedAt = Instant.now().minusSeconds(120);
        Instant expiredAt = Instant.now().minusSeconds(60);
        SecretKey secretKey = Keys.hmacShaKeyFor(authProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(String.valueOf(usuarioId))
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiredAt))
                .signWith(secretKey)
                .compact();
    }
}
