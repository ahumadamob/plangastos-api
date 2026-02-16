package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.PresupuestoRequestDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoDropdownDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.PresupuestoMapper;
import io.github.ahumadamob.plangastos.service.PresupuestoService;
import io.github.ahumadamob.plangastos.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "Presupuesto")
@RestController
@RequestMapping("/api/v1/presupuesto")
@Validated
public class PresupuestoController {

    private final PresupuestoService service;
    private final PresupuestoMapper mapper;

    public PresupuestoController(PresupuestoService service, PresupuestoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<PresupuestoResponseDto>>> getAll() {
        List<PresupuestoResponseDto> data = service.getAll().stream()
                .map(mapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de presupuestos"));
    }

    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponseSuccessDto<List<PresupuestoDropdownDto>>> getDropdown() {
        List<PresupuestoDropdownDto> data = service.getAllOrderByFechaDesdeDesc().stream()
                .map(mapper::entityToDropdownDto)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de presupuestos para dropdown"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PresupuestoResponseDto>> getById(@PathVariable Long id) {
        PresupuestoResponseDto data = mapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de presupuesto"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<PresupuestoResponseDto>> create(
            @Valid @RequestBody PresupuestoRequestDto request) {
        PresupuestoResponseDto data = mapper.entityToResponse(
                service.create(mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Presupuesto creado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PresupuestoResponseDto>> update(
            @PathVariable Long id, @Valid @RequestBody PresupuestoRequestDto request) {
        PresupuestoResponseDto data = mapper.entityToResponse(
                service.update(id, mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Presupuesto actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Presupuesto eliminado"));
    }
}
