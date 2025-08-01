package com.web.enums.operation;

public enum ComparisonOperator {
    // Basic operations
    EQUALS,
    NOT_EQUALS,
    GREATER_THAN,
    LESS_THAN,
    GREATER_THAN_OR_EQUAL_TO,
    LESS_THAN_OR_EQUAL_TO,
    LIKE,
    NOT_LIKE,
    CONTAINS,
    NOT_CONTAINS,
    IN,
    NOT_IN,
    IS_NULL,
    IS_NOT_NULL,
    BETWEEN,
    NOT_BETWEEN,

    // Average operations
    AVG_EQUALS,
    AVG_NOT_EQUALS,
    AVG_GREATER_THAN,
    AVG_LESS_THAN,
    AVG_GREATER_THAN_OR_EQUAL_TO,
    AVG_LESS_THAN_OR_EQUAL_TO,

    // Count operations
    COUNT_EQUALS,
    COUNT_NOT_EQUALS,
    COUNT_GREATER_THAN,
    COUNT_LESS_THAN,
    COUNT_GREATER_THAN_OR_EQUAL_TO,
    COUNT_LESS_THAN_OR_EQUAL_TO,

    // Sum operations
    SUM_EQUALS,
    SUM_NOT_EQUALS,
    SUM_GREATER_THAN,
    SUM_LESS_THAN,
    SUM_GREATER_THAN_OR_EQUAL_TO,
    SUM_LESS_THAN_OR_EQUAL_TO,

    // Max operations
    MAX_EQUALS,
    MAX_NOT_EQUALS,
    MAX_GREATER_THAN,
    MAX_LESS_THAN,
    MAX_GREATER_THAN_OR_EQUAL_TO,
    MAX_LESS_THAN_OR_EQUAL_TO,

    // Min operations
    MIN_EQUALS,
    MIN_NOT_EQUALS,
    MIN_GREATER_THAN,
    MIN_LESS_THAN,
    MIN_GREATER_THAN_OR_EQUAL_TO,
    MIN_LESS_THAN_OR_EQUAL_TO
}