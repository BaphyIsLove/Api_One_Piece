package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Ubication;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.UbicationRepository;
import com.api.onepiece.service.UbicationService;

import jakarta.validation.Valid;

@Controller
public class UbicationController {
    
    @Autowired
    UbicationRepository ubicationRepository;
    @Autowired
    UbicationService ubicationService;

    @GetMapping("/ubicationForm")
    public String ubicationForm(ModelMap model) {
        model.addAttribute("ubicationForm", new Ubication());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/ubicationForm")
    public String postUbicationForm(@Valid @ModelAttribute("ubicationForm")Ubication ubication, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("ubicationForm", ubication);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                ubicationService.create(ubication);
                model.addAttribute("ubicationForm", new Ubication());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("ubicationForm", ubication);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("ubicationForm", ubication);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("editUbication/{id}")
    public String ubicationForm(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            Ubication ubicationToEdit = ubicationService.getById(id);
            model.addAttribute("ubicationForm", ubicationToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("editUbication")
    public String postUbicationEdit(@Valid @ModelAttribute("ubicationForm")Ubication ubication, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("ubicationForm", ubication);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                ubicationService.update(ubication);
                model.addAttribute("ubicationForm", new Ubication());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("ubicationForm", ubication);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("ubicationForm", ubication);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteUbication/{id}")
    public String deleteUbication(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try{
            ubicationService.delete(id);
        } catch (MyEntityNotFoundException e){
            throw e;
        }
        return "redirect:/ubicationForm";
    }
    
    @GetMapping("/ubicationForm/cancel")
    public String cancelButon(){
        return "redirect:/ubicationForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showUbicationInfo", true);
        model.addAttribute("selectedFormOption", "Raza/Especie");
        model.addAttribute("ubicationList", ubicationService.getAll());
        
    }

}
