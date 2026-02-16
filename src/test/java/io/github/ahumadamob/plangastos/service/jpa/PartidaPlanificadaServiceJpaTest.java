package io.github.ahumadamob.plangastos.service.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.TransaccionRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.github.ahumadamob.plangastos.security.CurrentUserService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PartidaPlanificadaServiceJpaTest {

    @Mock
    private PartidaPlanificadaRepository partidaPlanificadaRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CurrentUserService currentUserService;

    @InjectMocks
    private PartidaPlanificadaServiceJpa partidaPlanificadaServiceJpa;

    @Test
    void consolidar_DebePropagarAjustesACopiasRelacionadas() {
        LocalDate hoy = LocalDate.now();

        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        PartidaPlanificada original = new PartidaPlanificada();
        original.setId(10L);
        original.setFechaObjetivo(hoy.minusMonths(1));
        original.setCuota(2);
        original.setCantidadCuotas(12);

        PartidaPlanificada copiaFutura = new PartidaPlanificada();
        copiaFutura.setId(11L);
        copiaFutura.setPartidaOrigen(original);
        copiaFutura.setFechaObjetivo(hoy.plusMonths(1));
        copiaFutura.setCuota(1);
        copiaFutura.setCantidadCuotas(12);

        PartidaPlanificada copiaSinCuotaValida = new PartidaPlanificada();
        copiaSinCuotaValida.setId(12L);
        copiaSinCuotaValida.setPartidaOrigen(original);
        copiaSinCuotaValida.setFechaObjetivo(hoy.plusMonths(20));
        copiaSinCuotaValida.setCuota(4);
        copiaSinCuotaValida.setCantidadCuotas(5);

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(10L, 1L)).thenReturn(Optional.of(original));
        when(transaccionRepository.sumMontoByPartidaPlanificadaId(10L)).thenReturn(BigDecimal.valueOf(2500));
        when(partidaPlanificadaRepository.findByPartidaOrigenIdAndUsuarioIdAndFechaObjetivoGreaterThanEqual(10L, 1L, hoy))
                .thenReturn(List.of(copiaFutura, copiaSinCuotaValida));

        PartidaPlanificada consolidada = partidaPlanificadaServiceJpa.consolidar(10L);

        assertThat(consolidada.getMontoComprometido()).isEqualByComparingTo("2500");
        assertThat(consolidada.getConsolidado()).isTrue();

        ArgumentCaptor<List<PartidaPlanificada>> captor = ArgumentCaptor.forClass(List.class);
        verify(partidaPlanificadaRepository).saveAll(captor.capture());

        List<PartidaPlanificada> actualizadas = captor.getValue();
        assertThat(actualizadas).hasSize(3);

        PartidaPlanificada actualizadaFutura = actualizadas.stream()
                .filter(p -> p.getId().equals(11L))
                .findFirst()
                .orElseThrow();
        assertThat(actualizadaFutura.getMontoComprometido()).isEqualByComparingTo("2500");
        assertThat(actualizadaFutura.getConsolidado()).isFalse();
        assertThat(actualizadaFutura.getCuota()).isEqualTo(4);

        PartidaPlanificada actualizadaSinCuotaValida = actualizadas.stream()
                .filter(p -> p.getId().equals(12L))
                .findFirst()
                .orElseThrow();
        assertThat(actualizadaSinCuotaValida.getMontoComprometido()).isEqualByComparingTo("2500");
        assertThat(actualizadaSinCuotaValida.getConsolidado()).isFalse();
        assertThat(actualizadaSinCuotaValida.getCuota()).isNull();
    }


    @Test
    void actualizarMontoComprometido_DebePersistirMontoCuandoSeEnviaValor() {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setId(20L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(20L, 1L)).thenReturn(Optional.of(partida));
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(partidaPlanificadaRepository.save(partida)).thenReturn(partida);

        PartidaPlanificada actualizada =
                partidaPlanificadaServiceJpa.actualizarMontoComprometido(20L, BigDecimal.valueOf(750), null);

        assertThat(actualizada.getMontoComprometido()).isEqualByComparingTo("750");
        verify(partidaPlanificadaRepository).save(partida);
    }

    @Test
    void actualizarMontoComprometido_DebeIncrementarMontoConPorcentajeSobreOriginal() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(21L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(21L, 1L)).thenReturn(Optional.of(partida));
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(partidaPlanificadaRepository.save(partida)).thenReturn(partida);

        PartidaPlanificada actualizada =
                partidaPlanificadaServiceJpa.actualizarMontoComprometido(21L, null, BigDecimal.valueOf(10));

        assertThat(actualizada.getMontoComprometido()).isEqualByComparingTo("1100");
    }

    @Test
    void actualizarMontoComprometido_DebePermitirPorcentajeNegativoHastaMenosCien() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(22L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(22L, 1L)).thenReturn(Optional.of(partida));
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(partidaPlanificadaRepository.save(partida)).thenReturn(partida);

        PartidaPlanificada actualizada =
                partidaPlanificadaServiceJpa.actualizarMontoComprometido(22L, null, BigDecimal.valueOf(-100));

        assertThat(actualizada.getMontoComprometido()).isEqualByComparingTo("0");
    }

    @Test
    void actualizarMontoComprometido_DebeFallarCuandoNoSeEnviaMontoNiPorcentaje() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(23L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(23L, 1L)).thenReturn(Optional.of(partida));

        assertThatThrownBy(() -> partidaPlanificadaServiceJpa.actualizarMontoComprometido(23L, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Debe enviar montoComprometido o porcentaje");
    }

    @Test
    void actualizarMontoComprometido_DebeFallarCuandoPorcentajeEsMenorAMenosCien() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(24L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(24L, 1L)).thenReturn(Optional.of(partida));

        assertThatThrownBy(() ->
                        partidaPlanificadaServiceJpa.actualizarMontoComprometido(24L, null, BigDecimal.valueOf(-100.01)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("porcentaje no puede ser menor a -100");
    }

    @Test
    void actualizarMontoComprometido_DebeFallarCuandoSeEnviaMontoYPorcentaje() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(25L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(25L, 1L)).thenReturn(Optional.of(partida));

        assertThatThrownBy(() -> partidaPlanificadaServiceJpa.actualizarMontoComprometido(
                        25L, BigDecimal.valueOf(800), BigDecimal.valueOf(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Solo debe enviar montoComprometido o porcentaje, no ambos");
    }


    @Test
    void create_DebeFallarCuandoHayAutoreferencia() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(1L);

        PartidaPlanificada origen = new PartidaPlanificada();
        origen.setId(1L);

        partida.setPartidaOrigen(origen);

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> partidaPlanificadaServiceJpa.create(partida))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Se detectó un ciclo en partidaOrigen");
    }

    @Test
    void create_DebeFallarCuandoHayCicloDeDosNodos() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(1L);

        PartidaPlanificada b = new PartidaPlanificada();
        b.setId(2L);

        PartidaPlanificada a = new PartidaPlanificada();
        a.setId(1L);

        partida.setPartidaOrigen(b);
        b.setPartidaOrigen(a);

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(2L, 1L)).thenReturn(Optional.of(b));
        when(partidaPlanificadaRepository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(a));

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> partidaPlanificadaServiceJpa.create(partida))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Se detectó un ciclo en partidaOrigen");
    }

    @Test
    void create_DebeFallarCuandoHayCicloDeTresNodos() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(1L);

        PartidaPlanificada b = new PartidaPlanificada();
        b.setId(2L);

        PartidaPlanificada c = new PartidaPlanificada();
        c.setId(3L);

        PartidaPlanificada a = new PartidaPlanificada();
        a.setId(1L);

        partida.setPartidaOrigen(b);
        b.setPartidaOrigen(c);
        c.setPartidaOrigen(a);

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(2L, 1L)).thenReturn(Optional.of(b));
        when(partidaPlanificadaRepository.findByIdAndUsuarioId(3L, 1L)).thenReturn(Optional.of(c));
        when(partidaPlanificadaRepository.findByIdAndUsuarioId(1L, 1L)).thenReturn(Optional.of(a));

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> partidaPlanificadaServiceJpa.create(partida))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Se detectó un ciclo en partidaOrigen");
    }

    @Test
    void create_DebePermitirJerarquiaSinCiclos() {
        PartidaPlanificada partida = new PartidaPlanificada();
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        partida.setId(1L);

        PartidaPlanificada b = new PartidaPlanificada();
        b.setId(2L);

        PartidaPlanificada c = new PartidaPlanificada();
        c.setId(3L);

        partida.setPartidaOrigen(b);
        b.setPartidaOrigen(c);

        when(partidaPlanificadaRepository.findByIdAndUsuarioId(2L, 1L)).thenReturn(Optional.of(b));
        when(partidaPlanificadaRepository.findByIdAndUsuarioId(3L, 1L)).thenReturn(Optional.of(c));
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(partidaPlanificadaRepository.save(partida)).thenReturn(partida);

        PartidaPlanificada resultado = partidaPlanificadaServiceJpa.create(partida);

        assertThat(resultado).isSameAs(partida);
    }

    @Test
    void create_DebePersistirConsolidadoEnFalsePorDefecto() {
        when(currentUserService.getCurrentUserId()).thenReturn(1L);

        PartidaPlanificada partida = new PartidaPlanificada();

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(partidaPlanificadaRepository.save(partida)).thenReturn(partida);

        PartidaPlanificada resultado = partidaPlanificadaServiceJpa.create(partida);

        assertThat(resultado.getConsolidado()).isFalse();
    }

}
