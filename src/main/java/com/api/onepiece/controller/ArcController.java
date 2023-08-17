package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Arc;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.ArcRepository;
import com.api.onepiece.repository.SagaRepository;
import com.api.onepiece.service.ArcService;

import jakarta.validation.Valid;

@Controller
public class ArcController {
    
    @Autowired
    ArcRepository arcRepository;

    @Autowired
    ArcService arcService;

    @Autowired
    SagaRepository sagaRepository;

    @GetMapping("/arcForm")
    public String arcForm(ModelMap model) throws Exception{
        model.addAttribute("arcForm", new Arc());
        model.addAttribute("listTab", "active");
        model.addAttribute("sagas", sagaRepository.findAll());
        prepareAttributesFormView(model);
        return "admin-pages/admin-page";
    }

    @PostMapping("/arcForm")
    public String createArc(@Valid @ModelAttribute("arcForm")Arc arc, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("arcForm", arc);
            model.addAttribute("sagas", sagaRepository.findAll());
            resetAttributesByValidationError(model);
        } else {
            try {
                arcService.createArc(arc);
                model.addAttribute("arcForm", new Arc());
                model.addAttribute("listTab", "active");
                prepareAttributesFormView(model);
            } catch (CustomFieldValidationException e) {
                result.rejectValue(e.getField(), null, e.getMessage());
                resetAttributesByValidationError(model);
                model.addAttribute("arcForm", arc);
                model.addAttribute("sagas", sagaRepository.findAll());
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e);
                resetAttributesByValidationError(model);
                model.addAttribute("arcForm", arc);
                model.addAttribute("sagas", sagaRepository.findAll());
            }
        }

        model.addAttribute("arcList", arcService.getAllArcs());
        model.addAttribute("sagas", sagaRepository.findAll());

        return "admin-pages/admin-page";
    }

    @GetMapping("/editArc/{id}")
    public String editArc(ModelMap model, @PathVariable(name="id")Long id) throws Exception{
        try {
            Arc arcToEdit = arcService.getArcById(id);
            model.addAttribute("arcForm", arcToEdit);
            model.addAttribute("sagas", sagaRepository.findAll());
            model.addAttribute("editMode", true);
            model.addAttribute("formTab", "active");
            prepareAttributesFormView(model);
        } catch (MyEntityNotFoundException exception) {           
            throw exception;
        } 
        return "admin-pages/admin-page";
    }

    @PostMapping("/editArc")
    public String postEditArc(@Valid @ModelAttribute("arcForm")Arc arc, BindingResult result, ModelMap model) throws Exception{
        try {
            if(result.hasErrors()){
                model.addAttribute("arcForm", arc);
                model.addAttribute("editMode", true);
                model.addAttribute("sagas", sagaRepository.findAll());
                resetAttributesByValidationError(model);
            } else {
                arcService.updateArc(arc);
                model.addAttribute("arcForm", new Arc());
                model.addAttribute("sagas", sagaRepository.findAll());
                model.addAttribute("listTab", "active");
                prepareAttributesFormView(model);
            }
        } catch (CustomFieldValidationException e) {
            result.rejectValue(e.getField(), null, e.getMessage());
            model.addAttribute("sagas", sagaRepository.findAll());
            model.addAttribute("sagaForm", arc);
            model.addAttribute("editMode", true);
            resetAttributesByValidationError(model);
        }

        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteArc/{id}")
    public String deleteArc(ModelMap model, @PathVariable(name = "id")Long id) throws Exception{
        try {
            arcService.deleteArc(id);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "redirect:/arcForm";
    }

    @GetMapping("/arcForm/cancel")
    public String cancelButton(ModelMap map){
        return "redirect:/arcForm";
    }

    private void prepareAttributesFormView(ModelMap model) {
        model.addAttribute("arcList", arcService.getAllArcs());
        model.addAttribute("selectedFormOption", "Arco");
        model.addAttribute("showArcsInfo", true);
    }

    private void resetAttributesByValidationError(ModelMap model){
        model.addAttribute("showArcsInfo", true);
        model.addAttribute("formTab", "active");
        model.addAttribute("selectedFormOption", "Arco");
    }

}
