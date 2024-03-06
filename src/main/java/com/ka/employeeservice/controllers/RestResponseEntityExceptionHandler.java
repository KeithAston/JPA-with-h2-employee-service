package com.ka.employeeservice.controllers;

import com.ka.employeeservice.exceptions.IllegalEmployeeArgumentException;
import com.ka.employeeservice.exceptions.ResourceNotFoundException;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@CommonsLog
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public RestResponseEntityExceptionHandler(){
        super();
    }

    @ExceptionHandler({ BadRequestException.class })
    public ResponseEntity<Object> handleInternal(final BadRequestException ex, final WebRequest request){
        final String bodyOfResponse = "Bad Request, please check details of request and try again";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ IllegalEmployeeArgumentException.class })
    public ResponseEntity<Object> handleInternal(final IllegalEmployeeArgumentException ex, final WebRequest request){
        final String bodyOfResponse = "One or more Employee Arguments are not valid, please check your request";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ ResourceNotFoundException.class })
    public ResponseEntity<Object> handleInternal(final ResourceNotFoundException ex, final WebRequest request){
        final String bodyOfResponse = "Requested Record not found";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException  ex, final WebRequest request){
        logger.error("500 status code", ex);
        final String bodyOfResponse = "Internal system error, please contact support";
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
