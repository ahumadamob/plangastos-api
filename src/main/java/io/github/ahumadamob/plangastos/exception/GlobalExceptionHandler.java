package io.github.ahumadamob.plangastos.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import io.github.ahumadamob.plangastos.dto.common.ErrorDetailDto;
import io.github.ahumadamob.plangastos.dto.common.ErrorResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Pattern COLUMN_PATTERN = Pattern.compile("Column '([^']+)'", Pattern.CASE_INSENSITIVE);
    private static final Pattern KEY_PATTERN = Pattern.compile("for key '([^']+)'", Pattern.CASE_INSENSITIVE);

    // 404 de dominio
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex,
            HttpServletRequest request) {
        List<ErrorDetailDto> errors = List.of(new ErrorDetailDto("error", ex.getMessage()));
        return buildResponse(HttpStatus.NOT_FOUND, errors, request);
    }

    // DTOs validados con @Valid en @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        List<ErrorDetailDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorDetailDto(fe.getField(), messageOrCode(fe)))
                .collect(Collectors.toList());

        // También agregamos errores globales (sin campo específico) si los hubiera
        ex.getBindingResult().getGlobalErrors().forEach(ge ->
                errors.add(new ErrorDetailDto(ge.getObjectName(), ge.getDefaultMessage()))
        );

        return buildResponse(HttpStatus.BAD_REQUEST, errors, request);
    }

    // Validación en parámetros (query/path) con @Validated + @NotBlank, @Min, etc.
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolation(ConstraintViolationException ex,
            HttpServletRequest request) {
        List<ErrorDetailDto> errors = ex.getConstraintViolations()
                .stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());

        return buildResponse(HttpStatus.BAD_REQUEST, errors, request);
    }

    // Binding/validación en @ModelAttribute o cuando no es RequestBody
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException ex, HttpServletRequest request) {
        List<ErrorDetailDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new ErrorDetailDto(fe.getField(), messageOrCode(fe)))
                .collect(Collectors.toList());

        ex.getBindingResult().getGlobalErrors().forEach(ge ->
                errors.add(new ErrorDetailDto(ge.getObjectName(), ge.getDefaultMessage()))
        );

        return buildResponse(HttpStatus.BAD_REQUEST, errors, request);
    }

    // Falta un parámetro requerido en query
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingParam(MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        List<ErrorDetailDto> errors = List.of(
                new ErrorDetailDto(ex.getParameterName(),
                        "Parámetro requerido ausente: " + ex.getParameterName())
        );
        return buildResponse(HttpStatus.BAD_REQUEST, errors, request);
    }

    // JSON malformado o tipo inválido en el body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleNotReadable(HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        List<ErrorDetailDto> errors = List.of(new ErrorDetailDto("body", "Cuerpo de la solicitud inválido o mal formado"));
        return buildResponse(HttpStatus.BAD_REQUEST, errors, request);
    }

    // Tipo inválido en path variable / query param (e.g., esperaba Long y llegó "abc")
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String name = ex.getName();
        String expected = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "tipo esperado";
        String msg = "Valor inválido para '" + name + "'. Se esperaba " + expected + ".";
        List<ErrorDetailDto> errors = List.of(new ErrorDetailDto(name, msg));
        return buildResponse(HttpStatus.BAD_REQUEST, errors, request);
    }

    // (Opcional) Método HTTP no soportado -> no es de validación pero útil
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        List<ErrorDetailDto> errors = List.of(new ErrorDetailDto("method", ex.getMessage()));
        return buildResponse(HttpStatus.METHOD_NOT_ALLOWED, errors, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex,
            HttpServletRequest request) {
        List<ErrorDetailDto> errors = List.of(new ErrorDetailDto("request", ex.getMessage()));
        return buildResponse(HttpStatus.BAD_REQUEST, errors, request);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleSqlIntegrity(SQLIntegrityConstraintViolationException ex,
            HttpServletRequest request) {
        String field = resolveConstraintField(ex.getMessage());
        List<ErrorDetailDto> errors = List.of(new ErrorDetailDto(field,
                "Violación de integridad de datos: " + ex.getMessage()));
        return buildResponse(HttpStatus.CONFLICT, errors, request);
    }

    // ---- Helpers ----

    private ErrorDetailDto toFieldError(ConstraintViolation<?> cv) {
        // Extrae el "leaf" del path como nombre de campo
        String field = StreamSupport.stream(cv.getPropertyPath().spliterator(), false)
                .reduce((first, second) -> second) // último nodo
                .map(node -> node.getName() != null ? node.getName() : "parameter")
                .orElse("parameter");
        String message = cv.getMessage();
        return new ErrorDetailDto(field, message);
    }

    private String messageOrCode(FieldError fe) {
        // Usa el defaultMessage si está, si no cae al código/rejectValue
        if (fe.getDefaultMessage() != null && !fe.getDefaultMessage().isBlank()) {
            return fe.getDefaultMessage();
        }
        return fe.getCode() != null ? fe.getCode() : "Valor inválido";
    }

    private ResponseEntity<ErrorResponseDto> buildResponse(HttpStatus status, List<ErrorDetailDto> messages,
            HttpServletRequest request) {
        ErrorResponseDto response = new ErrorResponseDto(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                messages,
                status.name(),
                request != null ? request.getRequestURI() : null);

        return ResponseEntity.status(status).body(response);
    }

    private String resolveConstraintField(String message) {
        if (message == null || message.isBlank()) {
            return "constraint";
        }

        Matcher columnMatcher = COLUMN_PATTERN.matcher(message);
        if (columnMatcher.find()) {
            return columnMatcher.group(1);
        }

        Matcher keyMatcher = KEY_PATTERN.matcher(message);
        if (keyMatcher.find()) {
            String key = keyMatcher.group(1);
            int lastDot = key.lastIndexOf('.');
            return lastDot >= 0 && lastDot + 1 < key.length() ? key.substring(lastDot + 1) : key;
        }

        return "constraint";
    }
}

