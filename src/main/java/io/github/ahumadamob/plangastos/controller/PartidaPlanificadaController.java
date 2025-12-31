package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaRequestDto;
import io.github.ahumadamob.plangastos.dto.PartidaPlanificadaResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.mapper.PartidaPlanificadaMapper;
import io.github.ahumadamob.plangastos.service.PartidaPlanificadaService;
import io.github.ahumadamob.plangastos.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Partida planificada")
@RestController
@RequestMapping("/api/v1/partida-planificada")
@Validated
public class PartidaPlanificadaController {

    private final PartidaPlanificadaService service;
    private final PartidaPlanificadaMapper mapper;

    public PartidaPlanificadaController(PartidaPlanificadaService service, PartidaPlanificadaMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/{presupuesto_id}/ingresos")
    public ResponseEntity<ApiResponseSuccessDto<List<PartidaPlanificadaResponseDto>>> getIngresosByPresupuesto(
            @PathVariable("presupuesto_id") Long presupuestoId) {
        List<PartidaPlanificadaResponseDto> data = service.getIngresosByPresupuestoId(presupuestoId).stream()
                .map(mapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de ingresos planificados por presupuesto"));
    }

    @GetMapping("/{presupuesto_id}/gastos")
    public ResponseEntity<ApiResponseSuccessDto<List<PartidaPlanificadaResponseDto>>> getGastosByPresupuesto(
            @PathVariable("presupuesto_id") Long presupuestoId) {
        List<PartidaPlanificadaResponseDto> data = service.getGastosByPresupuestoId(presupuestoId).stream()
                .map(mapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de gastos planificados por presupuesto"));
    }

    @GetMapping("/{presupuesto_id}/ahorro")
    public ResponseEntity<ApiResponseSuccessDto<List<PartidaPlanificadaResponseDto>>> getAhorroByPresupuesto(
            @PathVariable("presupuesto_id") Long presupuestoId) {
        List<PartidaPlanificadaResponseDto> data = service.getAhorroByPresupuestoId(presupuestoId).stream()
                .map(mapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de ahorro planificado por presupuesto"));
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<PartidaPlanificadaResponseDto>>> getAll() {
        List<PartidaPlanificadaResponseDto> data = service.getAll().stream()
                .map(mapper::entityToResponse)
                .toList();
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de partidas planificadas"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PartidaPlanificadaResponseDto>> getById(
            @PathVariable Long id) {
        PartidaPlanificadaResponseDto data = mapper.entityToResponse(service.getById(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Detalle de partida planificada"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseSuccessDto<PartidaPlanificadaResponseDto>> create(
            @RequestBody PartidaPlanificadaRequestDto request) {
        PartidaPlanificadaResponseDto data = mapper.entityToResponse(
                service.create(mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Partida planificada creada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PartidaPlanificadaResponseDto>> update(
            @PathVariable Long id, @RequestBody PartidaPlanificadaRequestDto request) {
        PartidaPlanificadaResponseDto data = mapper.entityToResponse(
                service.update(id, mapper.requestToEntity(request)));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Partida planificada actualizada"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<PartidaPlanificadaResponseDto>> consolidar(@PathVariable Long id) {
        PartidaPlanificadaResponseDto data = mapper.entityToResponse(service.consolidar(id));
        return ResponseEntity.ok(ApiResponseFactory.success(data, "Partida planificada consolidada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccessDto<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponseFactory.success(null, "Partida planificada eliminada"));
    }
}
