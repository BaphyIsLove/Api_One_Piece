package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Occupation;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.OccupationRepository;
import com.api.onepiece.service.OccupationService;

import jakarta.validation.Valid;

@Controller
public class OccupationController {
    
    @Autowired
    OccupationRepository occupationRepository;
    @Autowired
    OccupationService occupationService;

    @GetMapping("/occupationForm")
    public String occupationForm(ModelMap model) {
        model.addAttribute("occupationForm", new Occupation());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/occupationForm")
    public String postOccupationForm(@Valid @ModelAttribute("occupationForm")Occupation occupation, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("occupationForm", occupation);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                occupationService.create(occupation);
                model.addAttribute("occupationForm", new Occupation());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("occupationForm", occupation);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("occupationForm", occupation);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("editOccupation/{id}")
    public String occupationEdit(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            Occupation occupationToEdit = occupationService.getById(id);
            model.addAttribute("occupationForm", occupationToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("editOccupation")
    public String postOccupationEdit(@Valid @ModelAttribute("occupationForm")Occupation occupation, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("occupationForm", occupation);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                occupationService.update(occupation);
                model.addAttribute("occupationForm", new Occupation());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("occupationForm", occupation);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("occupationForm", occupation);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteOccupation/{id}")
    public String deleteOccupation(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try{
            occupationService.delete(id);
        } catch (MyEntityNotFoundException e){
            throw e;
        }
        return "redirect:/occupationForm";
    }
    
    @GetMapping("/occupationForm/cancel")
    public String cancelButon(){
        return "redirect:/occupationForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showOccupationInfo", true);
        model.addAttribute("selectedFormOption", "Ocupación/Trabajo");
        model.addAttribute("occupationList", occupationService.getAll());
    }

}