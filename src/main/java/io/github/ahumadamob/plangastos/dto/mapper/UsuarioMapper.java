package io.github.ahumadamob.plangastos.dto.mapper;

import io.github.ahumadamob.plangastos.dto.usuario.UsuarioRequestDto;
import io.github.ahumadamob.plangastos.dto.usuario.UsuarioResponseDto;
import io.github.ahumadamob.plangastos.entity.Usuario;

public class UsuarioMapper {

    public static Usuario requestToEntity(UsuarioRequestDto request) {
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        return usuario;
    }

    public static UsuarioResponseDto entityToResponse(Usuario usuario) {
        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setEmail(usuario.getEmail());
        response.setCreatedAt(usuario.getCreatedAt());
        response.setUpdatedAt(usuario.getUpdatedAt());
        return response;
    }
}
