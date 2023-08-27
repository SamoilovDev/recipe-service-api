package com.example.recipeservice.handler;

import com.example.recipeservice.dto.ApiErrorDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @Nullable HttpHeaders httpHeaders,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach( error ->
                        errors.put(((FieldError) error).getField(), error.getDefaultMessage())
                );

        return new ResponseEntity<>(
                ApiErrorDto.builder()
                        .httpStatus(statusCode.value())
                        .error("Bad Request")
                        .message(
                                errors.size() == 1
                                ? errors.getOrDefault("password", "")
                                : errors.entrySet().stream()
                                        .map(e -> e.getKey().concat(": ").concat(e.getValue()))
                                        .collect(Collectors.joining("\n"))
                        )
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build(),
                HttpStatus.BAD_REQUEST
        );

    }

    @Override
    public ResponseEntity<Object> handleTypeMismatch(
            @Nullable TypeMismatchException ex,
            @Nullable HttpHeaders headers,
            @Nullable HttpStatusCode status,
            WebRequest request
    ) {
        return ResponseEntity.badRequest().body(
                ApiErrorDto.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .error("Bad request")
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build()
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDto> handleConstraintViolationException(
            ConstraintViolationException ex,
            WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations()
                .forEach( constraintViolation ->
                errors.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage())
        );

        return ResponseEntity.badRequest().body(
                ApiErrorDto.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .error("Bad Request")
                        .message(errors.entrySet().stream()
                                .map(e -> e.getKey() + ": " + e.getValue())
                                .collect(Collectors.joining("; ")))
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDto> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            WebRequest request
    ) {
        return ResponseEntity.badRequest().body(
                ApiErrorDto.builder()
                        .httpStatus(HttpStatus.BAD_REQUEST.value())
                        .error("Bad Request")
                        .message(exception.getMessage())
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build()
        );
    }

}
