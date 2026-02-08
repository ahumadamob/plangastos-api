package io.github.ahumadamob.plangastos.service.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.TransaccionRepository;
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

    @InjectMocks
    private PartidaPlanificadaServiceJpa partidaPlanificadaServiceJpa;

    @Test
    void consolidar_DebePropagarAjustesACopiasRelacionadas() {
        LocalDate hoy = LocalDate.now();

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

        when(partidaPlanificadaRepository.findById(10L)).thenReturn(Optional.of(original));
        when(transaccionRepository.sumMontoByPartidaPlanificadaId(10L)).thenReturn(BigDecimal.valueOf(2500));
        when(partidaPlanificadaRepository.findByPartidaOrigenIdAndFechaObjetivoGreaterThanEqual(10L, hoy))
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
        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setId(20L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findById(20L)).thenReturn(Optional.of(partida));
        when(partidaPlanificadaRepository.save(partida)).thenReturn(partida);

        PartidaPlanificada actualizada =
                partidaPlanificadaServiceJpa.actualizarMontoComprometido(20L, BigDecimal.valueOf(750), null);

        assertThat(actualizada.getMontoComprometido()).isEqualByComparingTo("750");
        verify(partidaPlanificadaRepository).save(partida);
    }

    @Test
    void actualizarMontoComprometido_DebeIncrementarMontoConPorcentajeSobreOriginal() {
        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setId(21L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findById(21L)).thenReturn(Optional.of(partida));
        when(partidaPlanificadaRepository.save(partida)).thenReturn(partida);

        PartidaPlanificada actualizada =
                partidaPlanificadaServiceJpa.actualizarMontoComprometido(21L, null, BigDecimal.valueOf(10));

        assertThat(actualizada.getMontoComprometido()).isEqualByComparingTo("1100");
    }

    @Test
    void actualizarMontoComprometido_DebePermitirPorcentajeNegativoHastaMenosCien() {
        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setId(22L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findById(22L)).thenReturn(Optional.of(partida));
        when(partidaPlanificadaRepository.save(partida)).thenReturn(partida);

        PartidaPlanificada actualizada =
                partidaPlanificadaServiceJpa.actualizarMontoComprometido(22L, null, BigDecimal.valueOf(-100));

        assertThat(actualizada.getMontoComprometido()).isEqualByComparingTo("0");
    }

    @Test
    void actualizarMontoComprometido_DebeFallarCuandoNoSeEnviaMontoNiPorcentaje() {
        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setId(23L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findById(23L)).thenReturn(Optional.of(partida));

        assertThatThrownBy(() -> partidaPlanificadaServiceJpa.actualizarMontoComprometido(23L, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Debe enviar montoComprometido o porcentaje");
    }

    @Test
    void actualizarMontoComprometido_DebeFallarCuandoPorcentajeEsMenorAMenosCien() {
        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setId(24L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findById(24L)).thenReturn(Optional.of(partida));

        assertThatThrownBy(() ->
                        partidaPlanificadaServiceJpa.actualizarMontoComprometido(24L, null, BigDecimal.valueOf(-100.01)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("porcentaje no puede ser menor a -100");
    }

    @Test
    void actualizarMontoComprometido_DebeFallarCuandoSeEnviaMontoYPorcentaje() {
        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setId(25L);
        partida.setMontoComprometido(BigDecimal.valueOf(1000));

        when(partidaPlanificadaRepository.findById(25L)).thenReturn(Optional.of(partida));

        assertThatThrownBy(() -> partidaPlanificadaServiceJpa.actualizarMontoComprometido(
                        25L, BigDecimal.valueOf(800), BigDecimal.valueOf(10)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Solo debe enviar montoComprometido o porcentaje, no ambos");
    }

}
