package com.example.hrtool.exception;

import com.example.hrtool.payload.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<String>> handleException(Exception e) {
        return new ResponseEntity<>(
                new BaseResponse<>(false, "Fehler: " + e.getMessage(), null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

