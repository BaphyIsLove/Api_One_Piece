package com.api.onepiece.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.error.NameAlreadyExist;

@Controller
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MyEntityNotFoundException.class)
    public ModelAndView handleMyEntityNotFoundException(MyEntityNotFoundException ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorPage", true);
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(NameAlreadyExist.class)
    public ModelAndView handleNameAlreadyExist(NameAlreadyExist ex) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("errorMessage", ex.getMessage());
        return modelAndView;
    }
}
