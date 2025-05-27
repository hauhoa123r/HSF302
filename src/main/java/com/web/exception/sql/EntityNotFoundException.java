package com.web.exception.sql;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> clazz) {
        super(String.format("Entity not found: %s", clazz.getSimpleName()));
    }
}
