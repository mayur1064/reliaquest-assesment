package com.reliaquest.api.exception;

public class EmployeeAPIException extends RuntimeException {

    public EmployeeAPIException() {}

    public EmployeeAPIException(String message) {
        super(message);
    }
}
