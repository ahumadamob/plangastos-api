package io.github.ahumadamob.plangastos.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import io.github.ahumadamob.plangastos.entity.CuentaFinanciera;
import io.github.ahumadamob.plangastos.entity.Divisa;
import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.exception.ResourceNotFoundException;
import io.github.ahumadamob.plangastos.repository.CuentaFinancieraRepository;
import io.github.ahumadamob.plangastos.repository.DivisaRepository;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.PresupuestoRepository;
import io.github.ahumadamob.plangastos.repository.RubroRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;

@Component
public class MapperHelper {

    private final UsuarioRepository usuarioRepository;
    private final DivisaRepository divisaRepository;
    private final PresupuestoRepository presupuestoRepository;
    private final RubroRepository rubroRepository;
    private final CuentaFinancieraRepository cuentaFinancieraRepository;
    private final PartidaPlanificadaRepository partidaPlanificadaRepository;

    public MapperHelper(
            UsuarioRepository usuarioRepository,
            DivisaRepository divisaRepository,
            PresupuestoRepository presupuestoRepository,
            RubroRepository rubroRepository,
            CuentaFinancieraRepository cuentaFinancieraRepository,
            PartidaPlanificadaRepository partidaPlanificadaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.divisaRepository = divisaRepository;
        this.presupuestoRepository = presupuestoRepository;
        this.rubroRepository = rubroRepository;
        this.cuentaFinancieraRepository = cuentaFinancieraRepository;
        this.partidaPlanificadaRepository = partidaPlanificadaRepository;
    }

    public Usuario getUsuario(Long usuarioId) {
        return findOrThrow(usuarioRepository, usuarioId, "Usuario");
    }

    public Divisa getDivisa(Long divisaId) {
        return findOrThrow(divisaRepository, divisaId, "Divisa");
    }

    public Presupuesto getPresupuesto(Long presupuestoId) {
        return findOrThrow(presupuestoRepository, presupuestoId, "Presupuesto");
    }

    public Rubro getRubro(Long rubroId) {
        return findOrThrow(rubroRepository, rubroId, "Rubro");
    }

    public CuentaFinanciera getCuentaFinanciera(Long cuentaFinancieraId) {
        return findOrThrow(cuentaFinancieraRepository, cuentaFinancieraId, "Cuenta financiera");
    }

    public PartidaPlanificada getPartidaPlanificada(Long partidaPlanificadaId) {
        return findOrThrow(partidaPlanificadaRepository, partidaPlanificadaId, "Partida planificada");
    }

    public NaturalezaMovimiento getNaturalezaMovimiento(Long naturalezaId) {
        if (naturalezaId == null) {
            return null;
        }

        NaturalezaMovimiento[] valores = NaturalezaMovimiento.values();
        if (naturalezaId < 0 || naturalezaId >= valores.length) {
            throw new ResourceNotFoundException(
                    "Naturaleza de movimiento no encontrada con id " + naturalezaId);
        }

        return valores[naturalezaId.intValue()];
    }

    private <T> T findOrThrow(JpaRepository<T, Long> repository, Long id, String resourceName) {
        if (id == null) {
            return null;
        }
        return repository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(resourceName + " no encontrado con id " + id));
    }
}
