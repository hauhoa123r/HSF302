package com.web.exception.mapping;

public class ErrorMappingException extends RuntimeException {
    public ErrorMappingException(Class<?> source, Class<?> target) {
        super(String.format(
                "Error mapping from %s to %s",
                source.getSimpleName(),
                target.getSimpleName()
        ));
    }
}
