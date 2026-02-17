package io.github.ahumadamob.plangastos.mapper;

import io.github.ahumadamob.plangastos.dto.UsuarioRequestDto;
import io.github.ahumadamob.plangastos.dto.UsuarioResponseDto;
import io.github.ahumadamob.plangastos.entity.Usuario;

public class UsuarioMapper {

    public static Usuario requestToEntity(UsuarioRequestDto request) {
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPasswordHash(request.getPasswordHash());
        usuario.setActivo(request.getActivo() != null ? request.getActivo() : true);
        return usuario;
    }

    public static UsuarioResponseDto entityToResponse(Usuario usuario) {
        UsuarioResponseDto response = new UsuarioResponseDto();
        response.setId(usuario.getId());
        response.setEmail(usuario.getEmail());
        response.setActivo(usuario.getActivo());
        response.setCreatedAt(usuario.getCreatedAt());
        response.setUpdatedAt(usuario.getUpdatedAt());
        return response;
    }
}
