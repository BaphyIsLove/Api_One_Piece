package com.api.onepiece.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyEntityNotFoundException extends Exception{
    public MyEntityNotFoundException(String message){
        super(message);
    }
}
