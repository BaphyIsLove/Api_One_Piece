package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Crew;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.CrewRepository;
import com.api.onepiece.service.CrewService;

import jakarta.validation.Valid;

@Controller
public class CrewController {

    @Autowired
    CrewRepository crewRepository;
    @Autowired
    CrewService crewService;

    @GetMapping("/crewForm")
    public String crewForm(ModelMap model){
        model.addAttribute("crewForm", new Crew());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/crewForm")
    public String postCrewForm(@Valid @ModelAttribute("crewForm")Crew crew, BindingResult result ,ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("crewForm", crew);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                crewService.create(crew);
                model.addAttribute("crewForm", new Crew());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("crewForm", crew);
                prepareAttributesFormView(model, "formTab");
            } catch(Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("crewForm", crew);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/editCrew/{id}")
    public String crewEdit(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            Crew crewToEdit = crewService.getById(id);
            model.addAttribute("crewForm", crewToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("/editCrew")
    public String postCrewEdit(@Valid @ModelAttribute("crewForm")Crew crew, BindingResult result, ModelMap model) throws Exception{
        if(result.hasErrors()){
            model.addAttribute("crewForm", crew);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                crewService.update(crew);
                model.addAttribute("crewForm", new Crew());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("editMode", true);
                model.addAttribute("crewForm", crew);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteCrew/{id}")
    public String deleteCrew(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            crewService.delete(id);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "redirect:/crewForm";
    }

    @GetMapping("/crewForm/cancel")
    public String cancelBtn(){
        return "redirect:/crewForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showCrewInfo", true);
        model.addAttribute("selectedFormOption", "Grupo");
        model.addAttribute("crewList", crewService.getAll());
    }
    
}
