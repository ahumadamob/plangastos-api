package io.github.ahumadamob.plangastos.service.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.repository.RubroRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.github.ahumadamob.plangastos.security.CurrentUserService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RubroServiceJpaTest {

    @Mock
    private RubroRepository rubroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private RubroServiceJpa rubroServiceJpa;

    @Test
    void create_DebeFallarCuandoHayAutoreferencia() {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Rubro rubro = new Rubro();
        rubro.setId(1L);

        Rubro parent = new Rubro();
        parent.setId(1L);

        rubro.setParent(parent);

        assertThatThrownBy(() -> rubroServiceJpa.create(rubro))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Se detectó un ciclo en parent de Rubro");
    }

    @Test
    void create_DebeFallarCuandoHayCicloDeDosNodos() {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Rubro rubro = new Rubro();
        rubro.setId(1L);

        Rubro b = new Rubro();
        b.setId(2L);

        Rubro a = new Rubro();
        a.setId(1L);

        rubro.setParent(b);
        b.setParent(a);

        when(rubroRepository.findByIdAndUsuarioId(2L, 1L)).thenReturn(Optional.of(b));
        when(rubroRepository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(a));

        assertThatThrownBy(() -> rubroServiceJpa.create(rubro))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Se detectó un ciclo en parent de Rubro");
    }

    @Test
    void create_DebeFallarCuandoHayCicloDeTresNodos() {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Rubro rubro = new Rubro();
        rubro.setId(1L);

        Rubro b = new Rubro();
        b.setId(2L);

        Rubro c = new Rubro();
        c.setId(3L);

        Rubro a = new Rubro();
        a.setId(1L);

        rubro.setParent(b);
        b.setParent(c);
        c.setParent(a);

        when(rubroRepository.findByIdAndUsuarioId(2L, 1L)).thenReturn(Optional.of(b));
        when(rubroRepository.findByIdAndUsuarioId(3L, 1L)).thenReturn(Optional.of(c));
        when(rubroRepository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(a));

        assertThatThrownBy(() -> rubroServiceJpa.create(rubro))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Se detectó un ciclo en parent de Rubro");
    }

    @Test
    void create_DebePermitirJerarquiaSinCiclos() {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Rubro rubro = new Rubro();
        rubro.setId(1L);

        Rubro b = new Rubro();
        b.setId(2L);

        Rubro c = new Rubro();
        c.setId(3L);

        rubro.setParent(b);
        b.setParent(c);

        when(rubroRepository.findByIdAndUsuarioId(2L, 1L)).thenReturn(Optional.of(b));
        when(rubroRepository.findByIdAndUsuarioId(3L, 1L)).thenReturn(Optional.of(c));
        when(rubroRepository.save(rubro)).thenReturn(rubro);

        Rubro resultado = rubroServiceJpa.create(rubro);

        assertThat(resultado).isSameAs(rubro);
    }
}
