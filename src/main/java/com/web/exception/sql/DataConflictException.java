package com.web.exception.sql;

public class DataConflictException extends RuntimeException {
    public DataConflictException(Class<?> clazz) {
        super("Data conflict occurred for entity: " + clazz.getSimpleName());

    }
}
