package com.example.controller.advice;

import com.example.exception.DeActiveCompanyException;
import com.example.exception.DeActiveEmployeeException;
import com.example.exception.InvalidAgeEmployeeException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException handleException(Exception e){
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(InvalidAgeEmployeeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException handleInvalidAgeEmployeeException(Exception e){
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(DeActiveEmployeeException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseException handleDeActiveEmployeeException(Exception e){
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(DeActiveCompanyException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseException handleDeActiveCompanyException(Exception e){
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArgumentNotValidException(MethodArgumentNotValidException exception){
        String errorMessage = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" | "));

        return new ErrorResponse(errorMessage);
    }
}
