package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.exception.ResourceNotFoundException;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.TransaccionRepository;
import io.github.ahumadamob.plangastos.service.PartidaPlanificadaService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PartidaPlanificadaServiceJpa implements PartidaPlanificadaService {

    private final PartidaPlanificadaRepository partidaPlanificadaRepository;
    private final TransaccionRepository transaccionRepository;

    public PartidaPlanificadaServiceJpa(
            PartidaPlanificadaRepository partidaPlanificadaRepository, TransaccionRepository transaccionRepository) {
        this.partidaPlanificadaRepository = partidaPlanificadaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public List<PartidaPlanificada> getAll() {
        return partidaPlanificadaRepository.findAll();
    }

    @Override
    public PartidaPlanificada getById(Long id) {
        return partidaPlanificadaRepository.findById(id).orElse(null);
    }

    @Override
    public PartidaPlanificada create(PartidaPlanificada partidaPlanificada) {
        return partidaPlanificadaRepository.save(partidaPlanificada);
    }

    @Override
    public PartidaPlanificada update(Long id, PartidaPlanificada partidaPlanificada) {
        partidaPlanificada.setId(id);
        return partidaPlanificadaRepository.save(partidaPlanificada);
    }

    @Override
    public void delete(Long id) {
        partidaPlanificadaRepository.deleteById(id);
    }

    @Override
    public List<PartidaPlanificada> getIngresosByPresupuestoId(Long presupuestoId) {
        return partidaPlanificadaRepository.findByPresupuestoIdAndRubroNaturaleza(
                presupuestoId, NaturalezaMovimiento.INGRESO);
    }

    @Override
    public List<PartidaPlanificada> getGastosByPresupuestoId(Long presupuestoId) {
        return partidaPlanificadaRepository.findByPresupuestoIdAndRubroNaturaleza(
                presupuestoId, NaturalezaMovimiento.GASTO);
    }

    @Override
    public List<PartidaPlanificada> getAhorroByPresupuestoId(Long presupuestoId) {
        return partidaPlanificadaRepository.findByPresupuestoIdAndRubroNaturaleza(
                presupuestoId, NaturalezaMovimiento.RESERVA_AHORRO);
    }

    @Override
    public PartidaPlanificada actualizarMontoComprometido(Long id, BigDecimal montoComprometido, BigDecimal porcentaje) {
        PartidaPlanificada partida = partidaPlanificadaRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida planificada no encontrada con id " + id));

        validarActualizacionMonto(montoComprometido, porcentaje);

        BigDecimal montoOriginal = partida.getMontoComprometido();
        BigDecimal montoActualizado = montoComprometido != null
                ? montoComprometido
                : montoOriginal.add(montoOriginal.multiply(porcentaje).divide(BigDecimal.valueOf(100)));

        partida.setMontoComprometido(montoActualizado);
        return partidaPlanificadaRepository.save(partida);
    }

    @Override
    public PartidaPlanificada consolidar(Long id) {
        PartidaPlanificada partida = partidaPlanificadaRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Partida planificada no encontrada con id " + id));

        BigDecimal montoTotal = transaccionRepository.sumMontoByPartidaPlanificadaId(id);
        partida.setMontoComprometido(montoTotal);
        partida.setConsolidado(Boolean.TRUE);

        Long partidaOrigenId = partida.getPartidaOrigen() != null ? partida.getPartidaOrigen().getId() : partida.getId();
        List<PartidaPlanificada> partidasRelacionadas =
                partidaPlanificadaRepository.findByPartidaOrigenIdAndFechaObjetivoGreaterThanEqual(
                        partidaOrigenId, LocalDate.now());

        List<PartidaPlanificada> partidasActualizadas = new ArrayList<>();
        partidasActualizadas.add(partida);

        for (PartidaPlanificada copia : partidasRelacionadas) {
            if (copia.getId().equals(partida.getId())) {
                continue;
            }

            copia.setMontoComprometido(montoTotal);
            copia.setConsolidado(!esPartidaFutura(copia));
            copia.setCuota(calcularCuotaAjustada(partida, copia));
            partidasActualizadas.add(copia);
        }

        partidaPlanificadaRepository.saveAll(partidasActualizadas);
        return partida;
    }

    private void validarActualizacionMonto(BigDecimal montoComprometido, BigDecimal porcentaje) {
        if (montoComprometido == null && porcentaje == null) {
            throw new IllegalArgumentException("Debe enviar montoComprometido o porcentaje");
        }

        if (montoComprometido != null && porcentaje != null) {
            throw new IllegalArgumentException("Solo debe enviar montoComprometido o porcentaje, no ambos");
        }

        if (porcentaje != null && porcentaje.compareTo(BigDecimal.valueOf(-100)) < 0) {
            throw new IllegalArgumentException("porcentaje no puede ser menor a -100");
        }
    }

    private boolean esPartidaFutura(PartidaPlanificada partida) {
        return partida.getFechaObjetivo() == null || partida.getFechaObjetivo().isAfter(LocalDate.now());
    }

    private Integer calcularCuotaAjustada(PartidaPlanificada base, PartidaPlanificada copia) {
        if (base.getCuota() == null || base.getFechaObjetivo() == null || copia.getFechaObjetivo() == null) {
            return copia.getCuota();
        }

        long diferenciaMeses = ChronoUnit.MONTHS.between(
                base.getFechaObjetivo().withDayOfMonth(1), copia.getFechaObjetivo().withDayOfMonth(1));
        long cuotaAjustada = base.getCuota() + diferenciaMeses;

        if (cuotaAjustada <= 0 || cuotaAjustada > Integer.MAX_VALUE) {
            return null;
        }

        Integer cuotaCalculada = (int) cuotaAjustada;
        if (copia.getCantidadCuotas() != null && cuotaCalculada > copia.getCantidadCuotas()) {
            return null;
        }

        return cuotaCalculada;
    }
}
