package com.example.recipeservice.handler;

import com.example.recipeservice.model.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusExceptionHandler(ResponseStatusException ex,
                                                            HttpStatus statusCode,
                                                            WebRequest request) {
        return new ResponseEntity<>(
                ApiError.builder()
                        .httpStatus(statusCode.value())
                        .error(statusCode.toString())
                        .message(ex.getMessage())
                        .path(request.getDescription(false).replace("uri=", ""))
                        .build(),
                statusCode
        );
    }
}
