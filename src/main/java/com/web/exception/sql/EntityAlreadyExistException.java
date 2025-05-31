package com.web.exception.sql;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException(Class<?> clazz) {
        super(String.format("Entity of type %s already exists", clazz.getName()));
    }
}
