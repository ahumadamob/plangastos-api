package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.Transaccion;
import java.util.List;

public interface TransaccionService {

    List<Transaccion> getAll();

    Transaccion getById(Long id);

    Transaccion create(Transaccion transaccion);

    Transaccion update(Long id, Transaccion transaccion);

    void delete(Long id);
}
