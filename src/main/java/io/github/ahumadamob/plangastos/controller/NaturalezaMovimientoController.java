package io.github.ahumadamob.plangastos.controller;

import io.github.ahumadamob.plangastos.dto.NaturalezaMovimientoResponseDto;
import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.github.ahumadamob.plangastos.entity.NaturalezaMovimiento;
import io.github.ahumadamob.plangastos.util.ApiResponseFactory;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "NaturalezaMovimiento")
@RestController
@RequestMapping("/api/v1/naturaleza-movimiento")
public class NaturalezaMovimientoController {

    @GetMapping
    public ResponseEntity<ApiResponseSuccessDto<List<NaturalezaMovimientoResponseDto>>> getAll() {
        List<NaturalezaMovimientoResponseDto> data = Arrays.stream(NaturalezaMovimiento.values())
                .map(valor ->
                        new NaturalezaMovimientoResponseDto(
                                (long) valor.ordinal(), valor.name(), valor.getDescripcion()))
                .toList();

        return ResponseEntity.ok(ApiResponseFactory.success(data, "Listado de naturalezas de movimiento"));
    }
}
