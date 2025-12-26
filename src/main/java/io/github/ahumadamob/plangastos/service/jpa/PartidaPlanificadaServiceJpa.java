package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.repository.PartidaPlanificadaRepository;
import io.github.ahumadamob.plangastos.service.PartidaPlanificadaService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PartidaPlanificadaServiceJpa implements PartidaPlanificadaService {

    private final PartidaPlanificadaRepository partidaPlanificadaRepository;

    public PartidaPlanificadaServiceJpa(PartidaPlanificadaRepository partidaPlanificadaRepository) {
        this.partidaPlanificadaRepository = partidaPlanificadaRepository;
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
}
