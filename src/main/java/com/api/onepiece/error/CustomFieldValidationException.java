package com.api.onepiece.error;

import lombok.Getter;

@Getter
public class CustomFieldValidationException extends Exception{

    private String field;

    public CustomFieldValidationException(String message, String field){
        super(message);
        this.field = field;
    }
}
