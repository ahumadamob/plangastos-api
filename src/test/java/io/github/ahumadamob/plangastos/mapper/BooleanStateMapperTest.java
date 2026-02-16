package io.github.ahumadamob.plangastos.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.github.ahumadamob.plangastos.dto.CuentaFinancieraRequestDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoRequestDto;
import io.github.ahumadamob.plangastos.dto.RubroRequestDto;
import io.github.ahumadamob.plangastos.entity.Divisa;
import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.TipoCuenta;
import io.github.ahumadamob.plangastos.entity.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BooleanStateMapperTest {

    @Mock
    private MapperHelper mapperHelper;

    @Test
    void presupuestoMapper_DebeDefaultearInactivoEnFalse_CuandoNoVieneEnRequest() {
        PresupuestoMapper mapper = new PresupuestoMapper(mapperHelper);
        PresupuestoRequestDto request = new PresupuestoRequestDto();
        request.setNombre("Presupuesto");

        assertThat(mapper.requestToEntity(request).getInactivo()).isFalse();
    }

    @Test
    void rubroMapper_DebeDefaultearActivoEnFalse_CuandoNoVieneEnRequest() {
        RubroMapper mapper = new RubroMapper(mapperHelper);
        RubroRequestDto request = new RubroRequestDto();
        request.setNombre("Rubro");
        request.setNaturalezaMovimiento_id(1L);

        when(mapperHelper.getNaturalezaMovimiento(1L)).thenReturn(NaturalezaMovimiento.GASTO);

        assertThat(mapper.requestToEntity(request).getActivo()).isFalse();
    }

    @Test
    void cuentaFinancieraMapper_DebeDefaultearActivoEnFalse_CuandoNoVieneEnRequest() {
        CuentaFinancieraMapper mapper = new CuentaFinancieraMapper(mapperHelper);
        CuentaFinancieraRequestDto request = new CuentaFinancieraRequestDto();
        request.setUsuario_id(1L);
        request.setDivisa_id(2L);
        request.setNombre("Cuenta");
        request.setTipo(TipoCuenta.EFECTIVO);

        when(mapperHelper.getUsuario(1L)).thenReturn(new Usuario());
        when(mapperHelper.getDivisa(2L)).thenReturn(new Divisa());

        assertThat(mapper.requestToEntity(request).getActivo()).isFalse();
    }
}
