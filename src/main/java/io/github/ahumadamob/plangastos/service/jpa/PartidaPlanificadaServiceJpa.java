package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.exception.BusinessValidationException;
import io.github.ahumadamob.plangastos.exception.ResourceNotFoundException;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.TransaccionRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.github.ahumadamob.plangastos.security.CurrentUserService;
import io.github.ahumadamob.plangastos.service.PartidaPlanificadaService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class PartidaPlanificadaServiceJpa implements PartidaPlanificadaService {

    private final PartidaPlanificadaRepository partidaPlanificadaRepository;
    private final TransaccionRepository transaccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CurrentUserService currentUserService;

    public PartidaPlanificadaServiceJpa(
            PartidaPlanificadaRepository partidaPlanificadaRepository,
            TransaccionRepository transaccionRepository,
            UsuarioRepository usuarioRepository,
            CurrentUserService currentUserService) {
        this.partidaPlanificadaRepository = partidaPlanificadaRepository;
        this.transaccionRepository = transaccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<PartidaPlanificada> getAll() {
        Long usuarioId = currentUserService.getCurrentUserId();
        return partidaPlanificadaRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public PartidaPlanificada getById(Long id) {
        Long usuarioId = currentUserService.getCurrentUserId();
        return partidaPlanificadaRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Partida planificada no encontrada con id " + id));
    }

    @Override
    public PartidaPlanificada create(PartidaPlanificada partidaPlanificada) {
        Long usuarioId = currentUserService.getCurrentUserId();
        partidaPlanificada.setUsuario(usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId)));
        validarUsuarioConsistente(partidaPlanificada, usuarioId);
        validarJerarquiaSinCiclos(partidaPlanificada, usuarioId);
        return partidaPlanificadaRepository.save(partidaPlanificada);
    }

    @Override
    public PartidaPlanificada update(Long id, PartidaPlanificada partidaPlanificada) {
        Long usuarioId = currentUserService.getCurrentUserId();
        PartidaPlanificada existente = getById(id);
        partidaPlanificada.setId(existente.getId());
        partidaPlanificada.setUsuario(existente.getUsuario());
        validarUsuarioConsistente(partidaPlanificada, usuarioId);
        validarJerarquiaSinCiclos(partidaPlanificada, usuarioId);
        return partidaPlanificadaRepository.save(partidaPlanificada);
    }

    @Override
    public void delete(Long id) {
        PartidaPlanificada existente = getById(id);
        partidaPlanificadaRepository.deleteById(existente.getId());
    }

    @Override
    public List<PartidaPlanificada> getIngresosByPresupuestoId(Long presupuestoId) {
        Long usuarioId = currentUserService.getCurrentUserId();
        return partidaPlanificadaRepository.findByPresupuestoIdAndUsuarioIdAndRubroNaturaleza(
                presupuestoId, usuarioId, NaturalezaMovimiento.INGRESO);
    }

    @Override
    public List<PartidaPlanificada> getGastosByPresupuestoId(Long presupuestoId) {
        Long usuarioId = currentUserService.getCurrentUserId();
        return partidaPlanificadaRepository.findByPresupuestoIdAndUsuarioIdAndRubroNaturaleza(
                presupuestoId, usuarioId, NaturalezaMovimiento.GASTO);
    }

    @Override
    public List<PartidaPlanificada> getAhorroByPresupuestoId(Long presupuestoId) {
        Long usuarioId = currentUserService.getCurrentUserId();
        return partidaPlanificadaRepository.findByPresupuestoIdAndUsuarioIdAndRubroNaturaleza(
                presupuestoId, usuarioId, NaturalezaMovimiento.RESERVA_AHORRO);
    }

    @Override
    public PartidaPlanificada actualizarMontoComprometido(Long id, BigDecimal montoComprometido, BigDecimal porcentaje) {
        PartidaPlanificada partida = getById(id);

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
        Long usuarioId = currentUserService.getCurrentUserId();
        PartidaPlanificada partida = getById(id);

        BigDecimal montoTotal = transaccionRepository.sumMontoByPartidaPlanificadaId(id);
        partida.setMontoComprometido(montoTotal);
        partida.setConsolidado(true);

        Long partidaOrigenId = partida.getPartidaOrigen() != null ? partida.getPartidaOrigen().getId() : partida.getId();
        List<PartidaPlanificada> partidasRelacionadas =
                partidaPlanificadaRepository.findByPartidaOrigenIdAndUsuarioIdAndFechaObjetivoGreaterThanEqual(
                        partidaOrigenId, usuarioId, LocalDate.now());

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

    private void validarJerarquiaSinCiclos(PartidaPlanificada partidaPlanificada, Long usuarioId) {
        Set<Long> visitados = new HashSet<>();
        if (partidaPlanificada.getId() != null) {
            visitados.add(partidaPlanificada.getId());
        }

        PartidaPlanificada actual = partidaPlanificada.getPartidaOrigen();
        while (actual != null) {
            Long actualId = actual.getId();
            if (actualId != null && !visitados.add(actualId)) {
                throw new IllegalArgumentException("Se detectó un ciclo en partidaOrigen");
            }
            actual = actualId != null
                    ? partidaPlanificadaRepository.findByIdAndUsuarioId(actualId, usuarioId).orElse(actual.getPartidaOrigen())
                    : actual.getPartidaOrigen();
        }

        partidaPlanificada.validarJerarquiaSinCiclos();
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

    private void validarUsuarioConsistente(PartidaPlanificada partidaPlanificada, Long usuarioId) {
        if (partidaPlanificada.getPresupuesto() != null
                && partidaPlanificada.getPresupuesto().getUsuario() != null
                && !partidaPlanificada.getPresupuesto().getUsuario().getId().equals(usuarioId)) {
            throw new BusinessValidationException("El presupuesto no pertenece al usuario autenticado");
        }

        if (partidaPlanificada.getRubro() != null
                && partidaPlanificada.getRubro().getUsuario() != null
                && !partidaPlanificada.getRubro().getUsuario().getId().equals(usuarioId)) {
            throw new BusinessValidationException("El rubro no pertenece al usuario autenticado");
        }
    }
}
