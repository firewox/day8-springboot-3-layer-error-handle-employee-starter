package com.example.exception;

public class DeActiveCompanyException extends RuntimeException {
    public DeActiveCompanyException(String message) {
        super(message);
    }
}
