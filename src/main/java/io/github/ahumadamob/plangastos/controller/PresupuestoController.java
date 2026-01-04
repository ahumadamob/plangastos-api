package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.ApiResponsePresupuestoDropdownList;
import io.github.ahumadamob.plangastos.dto.ApiResponsePresupuestoItem;
import io.github.ahumadamob.plangastos.dto.ApiResponsePresupuestoList;
import io.github.ahumadamob.plangastos.dto.ApiResponseVoid;
import io.github.ahumadamob.plangastos.dto.PresupuestoItemDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoRequestDto;
import io.github.ahumadamob.plangastos.dto.PresupuestoDropdownDto;
import io.github.ahumadamob.plangastos.mapper.PresupuestoMapper;
import io.github.ahumadamob.plangastos.service.PresupuestoService;
import io.github.ahumadamob.plangastos.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Listar presupuestos (payload reducido)",
            responses =
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content =
                                    @Content(schema = @Schema(implementation = ApiResponsePresupuestoList.class))))
    @GetMapping
    public ResponseEntity<ApiResponsePresupuestoList> getAll() {
        List<PresupuestoItemDto> data = service.getAll().stream()
                .map(mapper::entityToResponse)
                .toList();
        ApiResponsePresupuestoList response = ApiResponseFactory.success(
                new ApiResponsePresupuestoList(), data, "Listado de presupuestos");
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listado ligero para selects",
            responses =
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content =
                                    @Content(
                                            schema =
                                                    @Schema(
                                                            implementation =
                                                                    ApiResponsePresupuestoDropdownList
                                                                            .class))))
    @GetMapping("/dropdown")
    public ResponseEntity<ApiResponsePresupuestoDropdownList> getDropdown() {
        List<PresupuestoDropdownDto> data = service.getAllOrderByFechaDesdeDesc().stream()
                .map(mapper::entityToDropdownDto)
                .toList();
        ApiResponsePresupuestoDropdownList response = ApiResponseFactory.success(
                new ApiResponsePresupuestoDropdownList(), data, "Listado de presupuestos para dropdown");
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener presupuesto (payload reducido)",
            responses =
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content =
                                    @Content(schema = @Schema(implementation = ApiResponsePresupuestoItem.class))))
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponsePresupuestoItem> getById(@PathVariable Long id) {
        PresupuestoItemDto data = mapper.entityToResponse(service.getById(id));
        ApiResponsePresupuestoItem response =
                ApiResponseFactory.success(new ApiResponsePresupuestoItem(), data, "Detalle de presupuesto");
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Crear presupuesto",
            responses =
                    @ApiResponse(
                            responseCode = "200",
                            description = "Creado",
                            content =
                                    @Content(schema = @Schema(implementation = ApiResponsePresupuestoItem.class))))
    @PostMapping
    public ResponseEntity<ApiResponsePresupuestoItem> create(@RequestBody PresupuestoRequestDto request) {
        PresupuestoItemDto data = mapper.entityToResponse(
                service.create(mapper.requestToEntity(request)));
        ApiResponsePresupuestoItem response =
                ApiResponseFactory.success(new ApiResponsePresupuestoItem(), data, "Presupuesto creado");
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar presupuesto",
            responses =
                    @ApiResponse(
                            responseCode = "200",
                            description = "Actualizado",
                            content =
                                    @Content(schema = @Schema(implementation = ApiResponsePresupuestoItem.class))))
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponsePresupuestoItem> update(
            @PathVariable Long id, @RequestBody PresupuestoRequestDto request) {
        PresupuestoItemDto data = mapper.entityToResponse(
                service.update(id, mapper.requestToEntity(request)));
        ApiResponsePresupuestoItem response =
                ApiResponseFactory.success(new ApiResponsePresupuestoItem(), data, "Presupuesto actualizado");
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar presupuesto",
            responses =
                    @ApiResponse(
                            responseCode = "200",
                            description = "Eliminado",
                            content = @Content(schema = @Schema(implementation = ApiResponseVoid.class))))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseVoid> delete(@PathVariable Long id) {
        service.delete(id);
        ApiResponseVoid response = ApiResponseFactory.success(new ApiResponseVoid(), null, "Presupuesto eliminado");
        return ResponseEntity.ok(response);
    }
}
