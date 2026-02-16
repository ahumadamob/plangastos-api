package io.github.ahumadamob.plangastos.service.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.entity.Transaccion;
import io.github.ahumadamob.plangastos.exception.BusinessValidationException;
import io.github.ahumadamob.plangastos.repository.TransaccionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceJpaTest {

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransaccionServiceJpa transaccionServiceJpa;

    @Test
    void create_DebePermitirCuandoPartidaPlanificadaEsConsistente() {
        Presupuesto presupuesto = presupuesto(1L);
        Rubro rubro = rubro(2L);

        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setPresupuesto(presupuesto);
        partida.setRubro(rubro);

        Transaccion transaccion = transaccion(presupuesto, rubro, partida);

        when(transaccionRepository.save(transaccion)).thenReturn(transaccion);

        Transaccion guardada = transaccionServiceJpa.create(transaccion);

        assertThat(guardada).isSameAs(transaccion);
        verify(transaccionRepository).save(transaccion);
    }

    @Test
    void create_DebeFallarCuandoPresupuestoNoCoincideConPartidaPlanificada() {
        Presupuesto presupuestoTransaccion = presupuesto(1L);
        Presupuesto presupuestoPartida = presupuesto(99L);
        Rubro rubro = rubro(2L);

        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setPresupuesto(presupuestoPartida);
        partida.setRubro(rubro);

        Transaccion transaccion = transaccion(presupuestoTransaccion, rubro, partida);

        assertThatThrownBy(() -> transaccionServiceJpa.create(transaccion))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessage("El presupuesto de la transacción debe coincidir con el de la partida planificada");
    }

    @Test
    void create_DebeFallarCuandoRubroNoCoincideConPartidaPlanificada() {
        Presupuesto presupuesto = presupuesto(1L);
        Rubro rubroTransaccion = rubro(2L);
        Rubro rubroPartida = rubro(77L);

        PartidaPlanificada partida = new PartidaPlanificada();
        partida.setPresupuesto(presupuesto);
        partida.setRubro(rubroPartida);

        Transaccion transaccion = transaccion(presupuesto, rubroTransaccion, partida);

        assertThatThrownBy(() -> transaccionServiceJpa.create(transaccion))
                .isInstanceOf(BusinessValidationException.class)
                .hasMessage("El rubro de la transacción debe coincidir con el de la partida planificada");
    }

    private Transaccion transaccion(Presupuesto presupuesto, Rubro rubro, PartidaPlanificada partidaPlanificada) {
        Transaccion transaccion = new Transaccion();
        transaccion.setPresupuesto(presupuesto);
        transaccion.setRubro(rubro);
        transaccion.setPartidaPlanificada(partidaPlanificada);
        transaccion.setCuenta(new CuentaFinanciera());
        return transaccion;
    }

    private Presupuesto presupuesto(Long id) {
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setId(id);
        return presupuesto;
    }

    private Rubro rubro(Long id) {
        Rubro rubro = new Rubro();
        rubro.setId(id);
        return rubro;
    }
}
