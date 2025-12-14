package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.PresupuestoRequestDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.PresupuestoMapper;
import io.github.ahumadamob.plangastos.service.PresupuestoService;
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
@RequestMapping("/api/v1/presupuestos")
@Validated
public class PresupuestoController {

    private final PresupuestoService service;

    public PresupuestoController(PresupuestoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<PresupuestoResponseDto>>> getAll() {
        List<PresupuestoResponseDto> data = service.getAll().stream()
                .map(PresupuestoMapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de presupuestos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PresupuestoResponseDto>> getById(@PathVariable Long id) {
        PresupuestoResponseDto data = PresupuestoMapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de presupuesto"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<PresupuestoResponseDto>> create(
            @RequestBody PresupuestoRequestDto request) {
        PresupuestoResponseDto data = PresupuestoMapper.entityToResponse(
                service.create(PresupuestoMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Presupuesto creado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PresupuestoResponseDto>> update(
            @PathVariable Long id, @RequestBody PresupuestoRequestDto request) {
        PresupuestoResponseDto data = PresupuestoMapper.entityToResponse(
                service.update(id, PresupuestoMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Presupuesto actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Presupuesto eliminado"));
    }
}
