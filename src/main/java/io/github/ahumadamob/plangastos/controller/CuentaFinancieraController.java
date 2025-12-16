package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.CuentaFinancieraRequestDto;
import io.github.ahumadamob.plangastos.dto.CuentaFinancieraResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.CuentaFinancieraMapper;
import io.github.ahumadamob.plangastos.service.CuentaFinancieraService;
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

@Tag(name = "Cuenta financiera")
@RestController
@RequestMapping("/api/v1/cuenta-financiera")
@Validated
public class CuentaFinancieraController {

    private final CuentaFinancieraService service;
    private final CuentaFinancieraMapper mapper;

    public CuentaFinancieraController(CuentaFinancieraService service, CuentaFinancieraMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<CuentaFinancieraResponseDto>>> getAll() {
        List<CuentaFinancieraResponseDto> data = service.getAll().stream()
                .map(mapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de cuentas financieras"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<CuentaFinancieraResponseDto>> getById(
            @PathVariable Long id) {
        CuentaFinancieraResponseDto data = mapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de cuenta financiera"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<CuentaFinancieraResponseDto>> create(
            @RequestBody CuentaFinancieraRequestDto request) {
        CuentaFinancieraResponseDto data = mapper.entityToResponse(
                service.create(mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Cuenta financiera creada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<CuentaFinancieraResponseDto>> update(
            @PathVariable Long id, @RequestBody CuentaFinancieraRequestDto request) {
        CuentaFinancieraResponseDto data = mapper.entityToResponse(
                service.update(id, mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Cuenta financiera actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Cuenta financiera eliminada"));
    }
}
