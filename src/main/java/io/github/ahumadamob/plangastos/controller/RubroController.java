package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.auth.CurrentUser;
import io.github.ahumadamob.plangastos.dto.RubroRequestDto;
import io.github.ahumadamob.plangastos.dto.RubroResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.RubroMapper;
import io.github.ahumadamob.plangastos.service.RubroService;
import io.github.ahumadamob.plangastos.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rubro")
@RestController
@RequestMapping("/api/v1/rubro")
@Validated
public class RubroController {

    private final RubroService service;
    private final RubroMapper mapper;

    public RubroController(RubroService service, RubroMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<RubroResponseDto>>> getAll(
            @AuthenticationPrincipal CurrentUser currentUser) {
        List<RubroResponseDto> data = service.getAllByUsuarioId(currentUser.id()).stream()
                .map(mapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de rubros"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<RubroResponseDto>> getById(@PathVariable Long id) {
        RubroResponseDto data = mapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de rubro"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<RubroResponseDto>> create(
            @RequestBody RubroRequestDto request) {
        RubroResponseDto data = mapper.entityToResponse(
                service.create(mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Rubro creado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<RubroResponseDto>> update(
            @PathVariable Long id, @RequestBody RubroRequestDto request) {
        RubroResponseDto data = mapper.entityToResponse(
                service.update(id, mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Rubro actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Rubro eliminado"));
    }
}
