package com.web.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Set;

public class MergeEntity<T> {

    private String[] getNullFields(Object source) {
        final BeanWrapper wrapper = new BeanWrapperImpl(source);
        PropertyDescriptor[] propertyDescriptors = wrapper.getPropertyDescriptors();
        Set<String> nullFields = new java.util.HashSet<>();

        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = wrapper.getPropertyValue(propertyName);
            if (propertyValue == null) {
                nullFields.add(propertyName);
            }
        }

        String[] result = new String[nullFields.size()];

        return nullFields.toArray(result);
    }

    public T merge(T source, T target) {
        BeanUtils.copyProperties(source, target, getNullFields(source));
        return source;
    }
}
