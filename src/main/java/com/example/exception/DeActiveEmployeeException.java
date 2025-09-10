package com.example.exception;

public class DeActiveEmployeeException extends RuntimeException {
    public DeActiveEmployeeException(String message) {
        super(message);
    }
}
