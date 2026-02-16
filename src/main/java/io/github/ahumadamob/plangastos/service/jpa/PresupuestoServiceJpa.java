package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.exception.BusinessValidationException;
import io.github.ahumadamob.plangastos.exception.ResourceNotFoundException;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.repository.PresupuestoRepository;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.github.ahumadamob.plangastos.security.CurrentUserService;
import io.github.ahumadamob.plangastos.service.PresupuestoService;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class PresupuestoServiceJpa implements PresupuestoService {

    private final PresupuestoRepository presupuestoRepository;
    private final PartidaPlanificadaRepository partidaPlanificadaRepository;
    private final UsuarioRepository usuarioRepository;
    private final CurrentUserService currentUserService;

    public PresupuestoServiceJpa(
            PresupuestoRepository presupuestoRepository,
            PartidaPlanificadaRepository partidaPlanificadaRepository,
            UsuarioRepository usuarioRepository,
            CurrentUserService currentUserService) {
        this.presupuestoRepository = presupuestoRepository;
        this.partidaPlanificadaRepository = partidaPlanificadaRepository;
        this.usuarioRepository = usuarioRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public List<Presupuesto> getAll() {
        Long usuarioId = currentUserService.getCurrentUserId();
        return presupuestoRepository.findByUsuarioIdAndInactivoFalse(usuarioId);
    }

    @Override
    public List<Presupuesto> getAllOrderByFechaDesdeDesc() {
        Long usuarioId = currentUserService.getCurrentUserId();
        return presupuestoRepository.findByUsuarioIdAndInactivoFalseOrderByFechaDesdeDesc(usuarioId);
    }

    @Override
    public Presupuesto getById(Long id) {
        Long usuarioId = currentUserService.getCurrentUserId();
        return presupuestoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Presupuesto no encontrado con id " + id));
    }

    @Override
    public Presupuesto create(Presupuesto presupuesto) {
        Long usuarioId = currentUserService.getCurrentUserId();
        presupuesto.setUsuario(usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + usuarioId)));

        if (presupuesto.getPresupuestoOrigen() != null
                && !Objects.equals(presupuesto.getPresupuestoOrigen().getUsuario().getId(), usuarioId)) {
            throw new BusinessValidationException("El presupuesto origen no pertenece al usuario autenticado");
        }

        validarRangoFechas(presupuesto);
        validarJerarquiaSinCiclos(presupuesto, usuarioId);
        Presupuesto nuevoPresupuesto = presupuestoRepository.save(presupuesto);

        if (presupuesto.getPresupuestoOrigen() != null) {
            copiarPartidasPlanificadas(nuevoPresupuesto, usuarioId);
        }

        return nuevoPresupuesto;
    }

    @Override
    public Presupuesto update(Long id, Presupuesto presupuesto) {
        Long usuarioId = currentUserService.getCurrentUserId();
        Presupuesto existente = getById(id);
        presupuesto.setId(existente.getId());
        presupuesto.setUsuario(existente.getUsuario());

        if (presupuesto.getPresupuestoOrigen() != null
                && !Objects.equals(presupuesto.getPresupuestoOrigen().getUsuario().getId(), usuarioId)) {
            throw new BusinessValidationException("El presupuesto origen no pertenece al usuario autenticado");
        }

        validarRangoFechas(presupuesto);
        validarJerarquiaSinCiclos(presupuesto, usuarioId);
        return presupuestoRepository.save(presupuesto);
    }

    @Override
    public void delete(Long id) {
        Presupuesto existente = getById(id);
        presupuestoRepository.deleteById(existente.getId());
    }

    private void validarRangoFechas(Presupuesto presupuesto) {
        LocalDate fechaDesde = presupuesto.getFechaDesde();
        LocalDate fechaHasta = presupuesto.getFechaHasta();

        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw new BusinessValidationException("fechaDesde debe ser anterior o igual a fechaHasta");
        }
    }

    private void validarJerarquiaSinCiclos(Presupuesto presupuesto, Long usuarioId) {
        Set<Long> visitados = new HashSet<>();
        if (presupuesto.getId() != null) {
            visitados.add(presupuesto.getId());
        }

        Presupuesto actual = presupuesto.getPresupuestoOrigen();
        while (actual != null) {
            if (actual.getUsuario() == null || !Objects.equals(actual.getUsuario().getId(), usuarioId)) {
                throw new BusinessValidationException("La jerarquía de presupuesto debe pertenecer al mismo usuario");
            }

            Long actualId = actual.getId();
            if (actualId != null && !visitados.add(actualId)) {
                throw new IllegalArgumentException("Se detectó un ciclo en presupuestoOrigen");
            }
            actual = actualId != null
                    ? presupuestoRepository.findByIdAndUsuarioId(actualId, usuarioId).orElse(actual.getPresupuestoOrigen())
                    : actual.getPresupuestoOrigen();
        }

        presupuesto.validarJerarquiaSinCiclos();
    }

    private void copiarPartidasPlanificadas(Presupuesto nuevoPresupuesto, Long usuarioId) {
        Presupuesto presupuestoOrigen = nuevoPresupuesto.getPresupuestoOrigen();
        List<PartidaPlanificada> partidasOrigen =
                partidaPlanificadaRepository.findByPresupuestoIdAndUsuarioId(presupuestoOrigen.getId(), usuarioId);

        if (partidasOrigen.isEmpty()) {
            return;
        }

        long mesesDiferencia = calcularDiferenciaEnMeses(
                presupuestoOrigen.getFechaDesde(), nuevoPresupuesto.getFechaDesde());

        List<PartidaPlanificada> partidasCopiadas = partidasOrigen.stream()
                .map(partidaOrigen -> crearPartidaCopiada(partidaOrigen, nuevoPresupuesto, mesesDiferencia))
                .toList();

        partidaPlanificadaRepository.saveAll(partidasCopiadas);
    }

    private long calcularDiferenciaEnMeses(LocalDate fechaOrigen, LocalDate fechaDestino) {
        if (fechaOrigen == null || fechaDestino == null) {
            return 0;
        }

        return ChronoUnit.MONTHS.between(
                fechaOrigen.withDayOfMonth(1), fechaDestino.withDayOfMonth(1));
    }

    private PartidaPlanificada crearPartidaCopiada(
            PartidaPlanificada partidaOrigen, Presupuesto nuevoPresupuesto, long mesesDiferencia) {
        PartidaPlanificada nuevaPartida = new PartidaPlanificada();
        nuevaPartida.setUsuario(nuevoPresupuesto.getUsuario());
        nuevaPartida.setPresupuesto(nuevoPresupuesto);
        nuevaPartida.setRubro(partidaOrigen.getRubro());
        nuevaPartida.setDescripcion(partidaOrigen.getDescripcion());
        nuevaPartida.setMontoComprometido(partidaOrigen.getMontoComprometido());
        nuevaPartida.setConsolidado(partidaOrigen.getConsolidado());
        nuevaPartida.setCantidadCuotas(partidaOrigen.getCantidadCuotas());

        if (partidaOrigen.getFechaObjetivo() != null) {
            nuevaPartida.setFechaObjetivo(partidaOrigen.getFechaObjetivo().plusMonths(mesesDiferencia));
        }

        if (partidaOrigen.getCuota() != null) {
            int cuotaBase = partidaOrigen.getCuota();
            long cuotaAjustada = cuotaBase + mesesDiferencia;
            nuevaPartida.setCuota(cuotaAjustada <= 0 ? null : (int) cuotaAjustada);
        }

        return nuevaPartida;
    }
}
