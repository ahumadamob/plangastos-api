package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaRequestDto;
import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.PartidaPlanificadaMapper;
import io.github.ahumadamob.plangastos.service.PartidaPlanificadaService;
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
@RequestMapping("/api/v1/partidas-planificadas")
@Validated
public class PartidaPlanificadaController {

    private final PartidaPlanificadaService service;

    public PartidaPlanificadaController(PartidaPlanificadaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<PartidaPlanificadaResponseDto>>> getAll() {
        List<PartidaPlanificadaResponseDto> data = service.getAll().stream()
                .map(PartidaPlanificadaMapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de partidas planificadas"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PartidaPlanificadaResponseDto>> getById(
            @PathVariable Long id) {
        PartidaPlanificadaResponseDto data = PartidaPlanificadaMapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de partida planificada"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<PartidaPlanificadaResponseDto>> create(
            @RequestBody PartidaPlanificadaRequestDto request) {
        PartidaPlanificadaResponseDto data = PartidaPlanificadaMapper.entityToResponse(
                service.create(PartidaPlanificadaMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Partida planificada creada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PartidaPlanificadaResponseDto>> update(
            @PathVariable Long id, @RequestBody PartidaPlanificadaRequestDto request) {
        PartidaPlanificadaResponseDto data = PartidaPlanificadaMapper.entityToResponse(
                service.update(id, PartidaPlanificadaMapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Partida planificada actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Partida planificada eliminada"));
    }
}
