package io.github.ahumadamob.plangastos.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.ahumadamob.plangastos.dto.UsuarioRequestDto;
import io.github.ahumadamob.plangastos.dto.UsuarioResponseDto;
import io.github.ahumadamob.plangastos.entity.Usuario;
import org.junit.jupiter.api.Test;

class UsuarioMapperTest {

    @Test
    void requestToEntity_DebeMapearCamposYDefaultearActivoEnTrue() {
        UsuarioRequestDto request = new UsuarioRequestDto();
        request.setNombre("Test User");
        request.setEmail("test@example.com");
        request.setPasswordHash("$2a$10$123456789012345678901212345678901234567890123456789012");

        Usuario usuario = UsuarioMapper.requestToEntity(request);

        assertThat(usuario.getNombre()).isEqualTo("Test User");
        assertThat(usuario.getEmail()).isEqualTo("test@example.com");
        assertThat(usuario.getPasswordHash()).isEqualTo("$2a$10$123456789012345678901212345678901234567890123456789012");
        assertThat(usuario.getActivo()).isTrue();
    }

    @Test
    void entityToResponse_NuncaDebeExponerPasswordHash() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        usuario.setEmail("test@example.com");
        usuario.setPasswordHash("secret");
        usuario.setActivo(true);

        UsuarioResponseDto response = UsuarioMapper.entityToResponse(usuario);

        assertThat(response.getNombre()).isEqualTo("Test User");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getActivo()).isTrue();
        assertThat(response.getClass().getDeclaredFields())
                .extracting("name")
                .doesNotContain("passwordHash");
    }
}
