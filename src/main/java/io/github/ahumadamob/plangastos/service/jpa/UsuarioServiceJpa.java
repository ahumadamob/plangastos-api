package io.github.ahumadamob.plangastos.service.jpa;

import io.github.ahumadamob.plangastos.entity.Usuario;
import io.github.ahumadamob.plangastos.repository.UsuarioRepository;
import io.github.ahumadamob.plangastos.service.UsuarioService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceJpa implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceJpa(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario getById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario getByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Usuario create(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario update(Long id, Usuario usuario) {
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }
}
