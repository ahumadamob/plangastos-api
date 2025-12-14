package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.Divisa;
import java.util.List;

public interface DivisaService {

    List<Divisa> getAll();

    Divisa getById(Long id);

    Divisa create(Divisa divisa);

    Divisa update(Long id, Divisa divisa);

    void delete(Long id);
}
