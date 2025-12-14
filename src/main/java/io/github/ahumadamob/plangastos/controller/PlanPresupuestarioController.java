package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.PlanPresupuestarioRequestDto;
import io.github.ahumadamob.plangastos.dto.PlanPresupuestarioResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.PlanPresupuestarioMapper;
import io.github.ahumadamob.plangastos.service.PlanPresupuestarioService;
import io.github.ahumadamob.plangastos.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Plan presupuestario")
@RestController
@RequestMapping("/api/v1/plan-presupuestario")
@Validated
public class PlanPresupuestarioController {

    private final PlanPresupuestarioService service;

    public PlanPresupuestarioController(PlanPresupuestarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<PlanPresupuestarioResponseDto>>> getAll() {
        List<PlanPresupuestarioResponseDto> data = service.getAll().stream()
                .map(PlanPresupuestarioMapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de planes presupuestarios"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PlanPresupuestarioResponseDto>> getById(
            @PathVariable Long id) {
        PlanPresupuestarioResponseDto data = PlanPresupuestarioMapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de plan presupuestario"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<PlanPresupuestarioResponseDto>> create(
            @RequestBody PlanPresupuestarioRequestDto request) {
        PlanPresupuestarioResponseDto data = PlanPresupuestarioMapper.entityToResponse(
                service.create(PlanPresupuestarioMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Plan presupuestario creado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PlanPresupuestarioResponseDto>> update(
            @PathVariable Long id, @RequestBody PlanPresupuestarioRequestDto request) {
        PlanPresupuestarioResponseDto data = PlanPresupuestarioMapper.entityToResponse(
                service.update(id, PlanPresupuestarioMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Plan presupuestario actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Plan presupuestario eliminado"));
    }
}
