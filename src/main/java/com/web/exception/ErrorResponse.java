package com.web.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse extends RuntimeException {
    private int errorCode = 500; // Default error code

    public ErrorResponse(String message) {
        super(message);
    }

    public ErrorResponse(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
