package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.DivisaRequestDto;
import io.github.ahumadamob.plangastos.dto.DivisaResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.DivisaMapper;
import io.github.ahumadamob.plangastos.service.DivisaService;
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

@Tag(name = "Divisa")
@RestController
@RequestMapping("/api/v1/divisas")
@Validated
public class DivisaController {

    private final DivisaService service;

    public DivisaController(DivisaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<DivisaResponseDto>>> getAll() {
        List<DivisaResponseDto> data = service.getAll().stream()
                .map(DivisaMapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de divisas"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<DivisaResponseDto>> getById(@PathVariable Long id) {
        DivisaResponseDto data = DivisaMapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de divisa"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<DivisaResponseDto>> create(
            @RequestBody DivisaRequestDto request) {
        DivisaResponseDto data = DivisaMapper.entityToResponse(
                service.create(DivisaMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Divisa creada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<DivisaResponseDto>> update(
            @PathVariable Long id, @RequestBody DivisaRequestDto request) {
        DivisaResponseDto data = DivisaMapper.entityToResponse(
                service.update(id, DivisaMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Divisa actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Divisa eliminada"));
    }
}
