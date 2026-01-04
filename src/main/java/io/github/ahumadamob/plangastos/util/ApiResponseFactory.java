package io.github.ahumadamob.plangastos.util;

import io.github.ahumadamob.plangastos.dto.common.ApiResponseSuccessDto;

/**
 * Factory helper to build standard API success responses.
 */
public final class ApiResponseFactory {

    private ApiResponseFactory() {
        // Utility class
    }

    public static <T> ApiResponseSuccessDto<T> success(T data, String message) {
        ApiResponseSuccessDto<T> response = new ApiResponseSuccessDto<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <R extends ApiResponseSuccessDto<T>, T> R success(R response, T data, String message) {
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
