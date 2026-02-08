package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.PresupuestoRepository;
import io.github.ahumadamob.plangastos.service.PresupuestoService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PresupuestoServiceJpa implements PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final PartidaPlanificadaRepository partidaPlanificadaRepository;

    public PresupuestoServiceJpa(
            PresupuestoRepository presupuestoRepository,
            PartidaPlanificadaRepository partidaPlanificadaRepository) {
        this.presupuestoRepository = presupuestoRepository;
        this.partidaPlanificadaRepository = partidaPlanificadaRepository;
    }

    @Override
    public List<Presupuesto> getAll() {
        return presupuestoRepository.findByInactivoIsNullOrInactivoFalse();
    }

    @Override
    public List<Presupuesto> getAllOrderByFechaDesdeDesc() {
        return presupuestoRepository.findByInactivoIsNullOrInactivoFalseOrderByFechaDesdeDesc();
    }

    @Override
    public Presupuesto getById(Long id) {
        return presupuestoRepository.findById(id).orElse(null);
    }

    @Override
    public Presupuesto create(Presupuesto presupuesto) {
        Presupuesto nuevoPresupuesto = presupuestoRepository.save(presupuesto);

        if (presupuesto.getPresupuestoOrigen() != null) {
            copiarPartidasPlanificadas(nuevoPresupuesto);
        }

        return nuevoPresupuesto;
    }

    @Override
    public Presupuesto update(Long id, Presupuesto presupuesto) {
        presupuesto.setId(id);
        return presupuestoRepository.save(presupuesto);
    }

    @Override
    public void delete(Long id) {
        presupuestoRepository.deleteById(id);
    }

    private void copiarPartidasPlanificadas(Presupuesto nuevoPresupuesto) {
        Presupuesto presupuestoOrigen = nuevoPresupuesto.getPresupuestoOrigen();
        List<PartidaPlanificada> partidasOrigen =
                partidaPlanificadaRepository.findByPresupuestoId(presupuestoOrigen.getId());

        if (partidasOrigen.isEmpty()) {
            return;
        }

        long mesesDiferencia = calcularDiferenciaEnMeses(
                presupuestoOrigen.getFechaDesde(), nuevoPresupuesto.getFechaDesde());

        List<PartidaPlanificada> partidasCopiadas = partidasOrigen.stream()
                .map(partidaOrigen -> crearPartidaCopiada(partidaOrigen, nuevoPresupuesto, mesesDiferencia))
                .flatMap(Optional::stream)
                .toList();

        partidaPlanificadaRepository.saveAll(partidasCopiadas);
    }

    private Optional<PartidaPlanificada> crearPartidaCopiada(
            PartidaPlanificada partidaOrigen, Presupuesto nuevoPresupuesto, long mesesDiferencia) {
        PartidaPlanificada partidaNueva = new PartidaPlanificada();
        partidaNueva.setPresupuesto(nuevoPresupuesto);
        partidaNueva.setRubro(partidaOrigen.getRubro());
        partidaNueva.setDescripcion(partidaOrigen.getDescripcion());
        partidaNueva.setMontoComprometido(partidaOrigen.getMontoComprometido());
        partidaNueva.setPartidaOrigen(partidaOrigen);
        partidaNueva.setConsolidado(Boolean.FALSE);
        partidaNueva.setCantidadCuotas(partidaOrigen.getCantidadCuotas());
        partidaNueva.setFechaObjetivo(ajustarFechaObjetivo(
                partidaOrigen.getFechaObjetivo(), mesesDiferencia));

        if (partidaOrigen.getCuota() != null && partidaOrigen.getCantidadCuotas() != null) {
            Integer cuotaAjustada = ajustarCuota(partidaOrigen.getCuota(), mesesDiferencia);
            if (cuotaAjustada == null
                    || cuotaAjustada <= 0
                    || cuotaAjustada > partidaOrigen.getCantidadCuotas()) {
                return Optional.empty();
            }
            partidaNueva.setCuota(cuotaAjustada);
        } else {
            partidaNueva.setCuota(partidaOrigen.getCuota());
        }

        return Optional.of(partidaNueva);
    }

    private long calcularDiferenciaEnMeses(LocalDate fechaOrigen, LocalDate fechaNueva) {
        if (Objects.isNull(fechaOrigen) || Objects.isNull(fechaNueva)) {
            return 0;
        }

        return ChronoUnit.MONTHS.between(
                fechaOrigen.withDayOfMonth(1), fechaNueva.withDayOfMonth(1));
    }

    private LocalDate ajustarFechaObjetivo(LocalDate fechaObjetivo, long mesesDiferencia) {
        if (fechaObjetivo == null) {
            return null;
        }
        return fechaObjetivo.plusMonths(mesesDiferencia);
    }

    private Integer ajustarCuota(Integer cuota, long mesesDiferencia) {
        long cuotaAjustada = cuota + mesesDiferencia;
        if (cuotaAjustada > Integer.MAX_VALUE || cuotaAjustada < Integer.MIN_VALUE) {
            return null;
        }
        return Math.toIntExact(cuotaAjustada);
    }
}
