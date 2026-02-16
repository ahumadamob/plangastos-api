package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.PartidaPlanificada;
import io.github.ahumadamob.plangastos.entity.Presupuesto;
import io.github.ahumadamob.plangastos.entity.Rubro;
import io.github.ahumadamob.plangastos.entity.Transaccion;
import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.exception.BusinessValidationException;
import io.github.ahumadamob.plangastos.repository.TransaccionRepository;
import io.github.ahumadamob.plangastos.service.TransaccionService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransaccionServiceJpa implements TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public TransaccionServiceJpa(TransaccionRepository transaccionRepository) {
        this.transaccionRepository = transaccionRepository;
    }

    @Override
    public List<Transaccion> getAll() {
        return transaccionRepository.findAll();
    }

    @Override
    public Transaccion getById(Long id) {
        return transaccionRepository.findById(id).orElse(null);
    }

    @Override
    public Transaccion create(Transaccion transaccion) {
        validateBusinessRules(transaccion);
        return transaccionRepository.save(transaccion);
    }

    @Override
    public Transaccion update(Long id, Transaccion transaccion) {
        transaccion.setId(id);
        validateBusinessRules(transaccion);
        return transaccionRepository.save(transaccion);
    }

    private void validateBusinessRules(Transaccion transaccion) {
        PartidaPlanificada partidaPlanificada = transaccion.getPartidaPlanificada();
        if (partidaPlanificada == null) {
            return;
        }

        validateMatch(
                transaccion.getPresupuesto(),
                partidaPlanificada.getPresupuesto(),
                "El presupuesto de la transacción debe coincidir con el de la partida planificada");
        validateMatch(
                transaccion.getRubro(),
                partidaPlanificada.getRubro(),
                "El rubro de la transacción debe coincidir con el de la partida planificada");

        Usuario usuarioCuenta = transaccion.getCuenta() == null ? null : transaccion.getCuenta().getUsuario();
        Usuario usuarioRubro = partidaPlanificada.getRubro() == null ? null : partidaPlanificada.getRubro().getUsuario();

        // Presupuesto aún no está modelado con usuario; esta validación se activará cuando ese contexto exista.
        validateOptionalUserMatch(usuarioCuenta, usuarioRubro,
                "La cuenta financiera y el rubro deben pertenecer al mismo usuario");
    }

    private void validateMatch(Presupuesto transaccionPresupuesto, Presupuesto partidaPresupuesto, String errorMessage) {
        Long transaccionId = transaccionPresupuesto == null ? null : transaccionPresupuesto.getId();
        Long partidaId = partidaPresupuesto == null ? null : partidaPresupuesto.getId();
        if (transaccionId == null || partidaId == null || !transaccionId.equals(partidaId)) {
            throw new BusinessValidationException(errorMessage);
        }
    }

    private void validateMatch(Rubro transaccionRubro, Rubro partidaRubro, String errorMessage) {
        Long transaccionId = transaccionRubro == null ? null : transaccionRubro.getId();
        Long partidaId = partidaRubro == null ? null : partidaRubro.getId();
        if (transaccionId == null || partidaId == null || !transaccionId.equals(partidaId)) {
            throw new BusinessValidationException(errorMessage);
        }
    }

    private void validateOptionalUserMatch(Usuario usuarioA, Usuario usuarioB, String errorMessage) {
        if (usuarioA == null || usuarioB == null) {
            return;
        }

        Long usuarioAId = usuarioA.getId();
        Long usuarioBId = usuarioB.getId();
        if (usuarioAId == null || usuarioBId == null || !usuarioAId.equals(usuarioBId)) {
            throw new BusinessValidationException(errorMessage);
        }
    }

    @Override
    public void delete(Long id) {
        transaccionRepository.deleteById(id);
    }
}
