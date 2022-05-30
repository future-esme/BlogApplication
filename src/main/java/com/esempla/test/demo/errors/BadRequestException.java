package com.esempla.test.demo.errors;

public class BadRequestException extends Exception{
    private static final long serialVersionUID = 1L;
    private final String entityName;
    private final String errorKey;

    public BadRequestException(String message, String entityName, String errorKey) {
        super(message);
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
