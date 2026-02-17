package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.Rubro;
import java.util.List;

public interface RubroService {

    List<Rubro> getAllByUsuarioId(Long usuarioId);

    Rubro getByIdAndUsuarioId(Long id, Long usuarioId);

    Rubro create(Rubro rubro);

    Rubro update(Long id, Long usuarioId, Rubro rubro);

    void delete(Long id, Long usuarioId);
}
