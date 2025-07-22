package com.web.apiadvice;

import com.web.exception.ErrorResponse;
import com.web.exception.page.PageNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PageNotFoundException.class)
    public String handlePageNotFoundException(PageNotFoundException ex) {
        return "Không tìm thấy!";
    }

    @ExceptionHandler(ErrorResponse.class)
    public ResponseEntity<String> handleErrorResponse(ErrorResponse ex) {
        return ResponseEntity.status(ex.getErrorCode())
                .body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest()
                .body(ex.getAllErrors().get(0).getDefaultMessage());
    }
}
