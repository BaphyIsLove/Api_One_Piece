package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Saga;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.repository.SagaRepository;
import com.api.onepiece.service.SagaServiceImpl;


import jakarta.validation.Valid;

@Controller
public class SagaController {

    @Autowired
    SagaServiceImpl sagaService;

    @Autowired
    SagaRepository sagaRepository;
    
    @GetMapping("/sagaForm")
    public String sagaForm(ModelMap model){
        model.addAttribute("sagaForm", new Saga());
        model.addAttribute("listTab", "active");
        prepareAttributesFormView(model);

        return "admin-pages/admin-page";
    }

    @PostMapping("/sagaForm")
    public String createSaga(@Valid @ModelAttribute("sagaForm")Saga saga, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("sagaForm", saga);
            resetAttributesByValidationError(model);
        } else{
            try {
                sagaService.create(saga);
                model.addAttribute("sagaForm", new Saga());
                model.addAttribute("listTab", "active");
                prepareAttributesFormView(model);
            } catch (CustomFieldValidationException e){
                result.rejectValue(e.getField(), null, e.getMessage());
                model.addAttribute("sagaForm", saga);
                resetAttributesByValidationError(model);

            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e);
                model.addAttribute("sagaForm", saga);
                resetAttributesByValidationError(model);
            }
        }
        model.addAttribute("sagaList", sagaService.getAll());

        return "admin-pages/admin-page";
    }

    @GetMapping("/editSaga/{id}")
    public String editSaga(ModelMap model, @PathVariable(name="id")Long id) throws Exception{ 
        try {
            Saga sagaToEdit = sagaService.getById(id);
            model.addAttribute("sagaForm", sagaToEdit);
            model.addAttribute("editMode", true);
            model.addAttribute("formTab", "active");
            prepareAttributesFormView(model);
        } catch (MyEntityNotFoundException exception) {           
            throw exception;
        } 
        return "admin-pages/admin-page";
    }

    @PostMapping("/editSaga")
    public String postEditSaga(@Valid @ModelAttribute("sagaForm") Saga saga, BindingResult result, ModelMap model) throws Exception {
        try {
            if (result.hasErrors()) {
                model.addAttribute("sagaForm", saga);
                model.addAttribute("editMode", true);
                resetAttributesByValidationError(model);
            } else{
                sagaService.update(saga);
                prepareAttributesFormView(model);
                model.addAttribute("sagaForm", new Saga());
                model.addAttribute("listTab", "active");
            }
        } catch (CustomFieldValidationException e) {
            result.rejectValue(e.getField(), null, e.getMessage());
            model.addAttribute("sagaList", sagaService.getAll());
            model.addAttribute("sagaForm", saga);
            model.addAttribute("editMode", true);
            resetAttributesByValidationError(model);
        }

        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteSaga/{id}")
    public String deleteSaga(ModelMap model, @PathVariable(name = "id")Long id) throws Exception{
        try {
            sagaService.delete(id);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "redirect:/sagaForm";
    }

    @GetMapping("/sagaForm/cancel")
    public String cancelButton(ModelMap map){
        return "redirect:/sagaForm";
    }

    protected void prepareAttributesFormView(ModelMap model){
        model.addAttribute("sagaList", sagaService.getAll());
        model.addAttribute("selectedFormOption", "saga");
        model.addAttribute("showSagasInfo", true);
        
    }

    protected void resetAttributesByValidationError(ModelMap model){
        model.addAttribute("showSagasInfo", true);
        model.addAttribute("formTab", "active");
        model.addAttribute("selectedFormOption", "saga");
    }

}
