package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.TransaccionRequestDto;
import io.github.ahumadamob.plangastos.dto.TransaccionResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.TransaccionMapper;
import io.github.ahumadamob.plangastos.service.TransaccionService;
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

@Tag(name = "Transacción")
@RestController
@RequestMapping("/api/v1/transaccion")
@Validated
public class TransaccionController {

    private final TransaccionService service;
    private final TransaccionMapper mapper;

    public TransaccionController(TransaccionService service, TransaccionMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<TransaccionResponseDto>>> getAll() {
        List<TransaccionResponseDto> data = service.getAll().stream()
                .map(mapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de transacciones"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<TransaccionResponseDto>> getById(@PathVariable Long id) {
        TransaccionResponseDto data = mapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de transacción"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<TransaccionResponseDto>> create(
            @RequestBody TransaccionRequestDto request) {
        TransaccionResponseDto data = mapper.entityToResponse(
                service.create(mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Transacción creada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<TransaccionResponseDto>> update(
            @PathVariable Long id, @RequestBody TransaccionRequestDto request) {
        TransaccionResponseDto data = mapper.entityToResponse(
                service.update(id, mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Transacción actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Transacción eliminada"));
    }
}
