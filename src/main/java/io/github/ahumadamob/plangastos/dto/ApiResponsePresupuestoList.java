package io.github.ahumadamob.plangastos.dto;

import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(name = "ApiResponsePresupuestoList")
public class ApiResponsePresupuestoList extends ApiResponseSuccessDto<List<PresupuestoItemDto>> {}
