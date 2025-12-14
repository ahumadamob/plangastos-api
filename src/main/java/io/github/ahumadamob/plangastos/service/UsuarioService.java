package io.github.ahumadamob.plangastos.service;

import io.github.ahumadamob.plangastos.entity.Usuario;
import java.util.List;

public interface UsuarioService {

    List<Usuario> getAll();

    Usuario getById(Long id);

    Usuario create(Usuario usuario);

    Usuario update(Long id, Usuario usuario);

    void delete(Long id);
}
