package com.web.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import com.web.exception.ResourceNotFoundException;

@Component
public class CheckFieldObject {
    public void check(Class<?> objectClass, Object object, String... fieldNames) {
        if (object == null) {
            throw new ResourceNotFoundException(String.format("%s cannot be null", objectClass.getSimpleName()));
        }

        BeanWrapper beanWrapper = new BeanWrapperImpl(object);
        for (String fieldName : fieldNames) {
            if (beanWrapper.getPropertyValue(fieldName) == null) {
                throw new ResourceNotFoundException(String.format("Resource not found: %s with field '%s'",
                        objectClass.getSimpleName(), fieldName));
            }
        }
    }
}
