package com.api.onepiece.error;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ViewExceptionHandler {
    
    @ExceptionHandler(MyEntityNotFoundException.class)
    public void handlerMyEntitiyNotFoundException(MyEntityNotFoundException exception, ModelMap model){
    }
}
