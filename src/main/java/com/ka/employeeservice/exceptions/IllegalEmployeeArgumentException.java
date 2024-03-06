package com.ka.employeeservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalEmployeeArgumentException extends Exception{
    public IllegalEmployeeArgumentException(String message){
        super(message);
    }
}
