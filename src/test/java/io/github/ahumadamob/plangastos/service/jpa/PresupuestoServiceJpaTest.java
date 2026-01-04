package io.github.ahumadamob.plangastos.service.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.PresupuestoRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PresupuestoServiceJpaTest {

    @Mock
    private PresupuestoRepository presupuestoRepository;

    @Mock
    private PartidaPlanificadaRepository partidaPlanificadaRepository;

    @InjectMocks
    private PresupuestoServiceJpa presupuestoServiceJpa;

    @Test
    void create_WhenNoPresupuestoOrigen_ShouldNotCopiarPartidas() {
        Presupuesto presupuesto = new Presupuesto();

        when(presupuestoRepository.save(presupuesto)).thenReturn(presupuesto);

        presupuestoServiceJpa.create(presupuesto);

        verify(presupuestoRepository).save(presupuesto);
        verify(partidaPlanificadaRepository, never()).findByPresupuestoId(any());
        verify(partidaPlanificadaRepository, never()).saveAll(any());
    }

    @Test
    void create_WhenTienePresupuestoOrigen_ShouldCopiarPartidasConFechaAjustada() {
        Presupuesto presupuestoOrigen = new Presupuesto();
        presupuestoOrigen.setId(1L);
        presupuestoOrigen.setFechaDesde(LocalDate.of(2025, 3, 1));

        Presupuesto nuevoPresupuesto = new Presupuesto();
        nuevoPresupuesto.setPresupuestoOrigen(presupuestoOrigen);
        nuevoPresupuesto.setFechaDesde(LocalDate.of(2025, 10, 1));

        PartidaPlanificada partidaA = new PartidaPlanificada();
        partidaA.setDescripcion("Partida A");
        partidaA.setMontoComprometido(BigDecimal.TEN);
        partidaA.setFechaObjetivo(LocalDate.of(2025, 3, 10));

        PartidaPlanificada partidaB = new PartidaPlanificada();
        partidaB.setDescripcion("Partida B");
        partidaB.setMontoComprometido(BigDecimal.ONE);
        partidaB.setFechaObjetivo(LocalDate.of(2025, 2, 25));

        when(presupuestoRepository.save(nuevoPresupuesto))
                .thenAnswer(invocation -> {
                    Presupuesto presupuesto = invocation.getArgument(0);
                    presupuesto.setId(2L);
                    return presupuesto;
                });
        when(partidaPlanificadaRepository.findByPresupuestoId(1L)).thenReturn(List.of(partidaA, partidaB));

        presupuestoServiceJpa.create(nuevoPresupuesto);

        ArgumentCaptor<List<PartidaPlanificada>> partidasCaptor = ArgumentCaptor.forClass(List.class);
        verify(partidaPlanificadaRepository).saveAll(partidasCaptor.capture());

        List<PartidaPlanificada> partidasCopiadas = partidasCaptor.getValue();
        assertThat(partidasCopiadas).hasSize(2);

        PartidaPlanificada copiaA = partidasCopiadas.get(0);
        PartidaPlanificada copiaB = partidasCopiadas.get(1);

        assertThat(copiaA.getPresupuesto().getId()).isEqualTo(2L);
        assertThat(copiaB.getPresupuesto().getId()).isEqualTo(2L);
        assertThat(copiaA.getDescripcion()).isEqualTo("Partida A");
        assertThat(copiaB.getDescripcion()).isEqualTo("Partida B");
        assertThat(copiaA.getFechaObjetivo()).isEqualTo(LocalDate.of(2025, 10, 10));
        assertThat(copiaB.getFechaObjetivo()).isEqualTo(LocalDate.of(2025, 9, 25));
        assertThat(copiaA.getTransacciones()).isEmpty();
        assertThat(copiaB.getTransacciones()).isEmpty();
    }

    @Test
    void create_WhenPartidaTieneCuotas_DebeAjustarCuotaYExcluirLasQueSuperanCantidad() {
        Presupuesto presupuestoOrigen = new Presupuesto();
        presupuestoOrigen.setId(3L);
        presupuestoOrigen.setFechaDesde(LocalDate.of(2025, 1, 1));

        Presupuesto nuevoPresupuesto = new Presupuesto();
        nuevoPresupuesto.setPresupuestoOrigen(presupuestoOrigen);
        nuevoPresupuesto.setFechaDesde(LocalDate.of(2025, 4, 1)); // +3 meses

        PartidaPlanificada partidaConCuotaValida = new PartidaPlanificada();
        partidaConCuotaValida.setDescripcion("Cuota valida");
        partidaConCuotaValida.setMontoComprometido(BigDecimal.valueOf(50));
        partidaConCuotaValida.setFechaObjetivo(LocalDate.of(2025, 1, 15));
        partidaConCuotaValida.setCuota(1);
        partidaConCuotaValida.setCantidadCuotas(3);

        PartidaPlanificada partidaQueSuperaCantidad = new PartidaPlanificada();
        partidaQueSuperaCantidad.setDescripcion("Cuota invalida");
        partidaQueSuperaCantidad.setMontoComprometido(BigDecimal.valueOf(30));
        partidaQueSuperaCantidad.setFechaObjetivo(LocalDate.of(2025, 2, 10));
        partidaQueSuperaCantidad.setCuota(3);
        partidaQueSuperaCantidad.setCantidadCuotas(4);

        when(presupuestoRepository.save(nuevoPresupuesto))
                .thenAnswer(invocation -> {
                    Presupuesto presupuesto = invocation.getArgument(0);
                    presupuesto.setId(4L);
                    return presupuesto;
                });
        when(partidaPlanificadaRepository.findByPresupuestoId(3L))
                .thenReturn(List.of(partidaConCuotaValida, partidaQueSuperaCantidad));

        presupuestoServiceJpa.create(nuevoPresupuesto);

        ArgumentCaptor<List<PartidaPlanificada>> partidasCaptor = ArgumentCaptor.forClass(List.class);
        verify(partidaPlanificadaRepository).saveAll(partidasCaptor.capture());

        List<PartidaPlanificada> partidasCopiadas = partidasCaptor.getValue();
        assertThat(partidasCopiadas).hasSize(1);
        PartidaPlanificada copia = partidasCopiadas.get(0);

        assertThat(copia.getDescripcion()).isEqualTo("Cuota valida");
        assertThat(copia.getCuota()).isEqualTo(4); // 1 + 3 meses
        assertThat(copia.getCantidadCuotas()).isEqualTo(3);
        assertThat(copia.getFechaObjetivo()).isEqualTo(LocalDate.of(2025, 4, 15));
    }
}
