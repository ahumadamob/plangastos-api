package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.UsuarioRequestDto;
import io.github.ahumadamob.plangastos.dto.UsuarioResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.UsuarioMapper;
import io.github.ahumadamob.plangastos.service.UsuarioService;
import io.github.ahumadamob.plangastos.util.ApiResponseFactory;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<UsuarioResponseDto>>> getAll() {
        List<UsuarioResponseDto> data = service.getAll().stream()
                .map(UsuarioMapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de usuarios"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<UsuarioResponseDto>> getById(@PathVariable Long id) {
        UsuarioResponseDto data = UsuarioMapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de usuario"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<UsuarioResponseDto>> create(
            @RequestBody UsuarioRequestDto request) {
        UsuarioResponseDto data = UsuarioMapper.entityToResponse(
                service.create(UsuarioMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Usuario creado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<UsuarioResponseDto>> update(
            @PathVariable Long id, @RequestBody UsuarioRequestDto request) {
        UsuarioResponseDto data = UsuarioMapper.entityToResponse(
                service.update(id, UsuarioMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Usuario actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Usuario eliminado"));
    }
}
