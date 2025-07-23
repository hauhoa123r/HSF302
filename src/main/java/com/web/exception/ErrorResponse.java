package com.web.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse extends RuntimeException {
    private int errorCode = 500; // Default error code
    private String toastType = "error"; // Default toast type
    private String urlRedirect; // Optional URL redirect, not used in this class

    public ErrorResponse(String message) {
        super(message);
    }

    public ErrorResponse(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorResponse(String toastType, String message) {
        super(message);
        this.toastType = toastType;
    }

    public ErrorResponse(String toastType, String message, String urlRedirect) {
        super(message);
        this.toastType = toastType;
        this.urlRedirect = urlRedirect;
    }
}
