package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.Rubro;
import java.util.List;

public interface RubroService {

    List<Rubro> getAll();

    Rubro getById(Long id);

    Rubro create(Rubro rubro);

    Rubro update(Long id, Rubro rubro);

    void delete(Long id);
}
