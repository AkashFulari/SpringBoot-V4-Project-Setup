package com.firstest.demo.exception;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.firstest.demo.responce.ApiResp;
import com.firstest.demo.service.Resp;

import tools.jackson.databind.exc.InvalidFormatException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResp<Void>> validation(MethodArgumentNotValidException ex) {
        List<FieldError> fields = ex.getBindingResult().getFieldErrors();
        return Resp.error(fields.get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResp<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            String allowed = Arrays.toString(ex.getRequiredType().getEnumConstants());
            return Resp.error(ex.getName() + " must be one of " + allowed, HttpStatus.BAD_REQUEST);
        }

        return Resp.error(
                "Invalid value",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResp<Void>> handleInvalidBody(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalid) {
            Class<?> target = invalid.getTargetType();
            if (target.isEnum()) {
                String field = invalid.getPath()
                        .get(0)
                        .getPropertyName();
                String allowed = Arrays.toString(
                        target.getEnumConstants());
                return Resp.error(field + " must be one of " + allowed, HttpStatus.BAD_REQUEST);
            }
        }

        return Resp.error("Invalid request body", HttpStatus.BAD_REQUEST);
    }

}