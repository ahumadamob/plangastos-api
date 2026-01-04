package io.github.ahumadamob.plangastos.dto;

import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiResponseVoid")
public class ApiResponseVoid extends ApiResponseSuccessDto<Object> {}
