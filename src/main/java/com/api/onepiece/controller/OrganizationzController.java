package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Organizationz;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.OrganizationzRepository;
import com.api.onepiece.service.OrganizationzService;

import jakarta.validation.Valid;

@Controller
public class OrganizationzController {
      
    @Autowired
    OrganizationzRepository organizationzRepository;
    @Autowired
    OrganizationzService organizationzService;

    @GetMapping("/organizationzForm")
    public String organizationzForm(ModelMap model) {
        model.addAttribute("organizationzForm", new Organizationz());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/organizationzForm")
    public String postOrganizationzForm(@Valid @ModelAttribute("organizationzForm")Organizationz organizationz, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("organizationzForm", organizationz);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                organizationzService.create(organizationz);
                model.addAttribute("organizationzForm", new Organizationz());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("organizationzForm", organizationz);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("organizationzForm", organizationz);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("editOrganizationz/{id}")
    public String organizationzForm(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            Organizationz organizationzToEdit = organizationzService.getById(id);
            model.addAttribute("organizationzForm", organizationzToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("editOrganizationz")
    public String postOrganizationzEdit(@Valid @ModelAttribute("organizationzForm")Organizationz organizationz, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("organizationzForm", organizationz);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                organizationzService.update(organizationz);
                model.addAttribute("organizationzForm", new Organizationz());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("organizationzForm", organizationz);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("organizationzForm", organizationz);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteOrganizationz/{id}")
    public String deleteOrganizationz(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try{
            organizationzService.delete(id);
        } catch (MyEntityNotFoundException e){
            throw e;
        }
        return "redirect:/organizationzForm";
    }
    
    @GetMapping("/organizationzForm/cancel")
    public String cancelButon(){
        return "redirect:/organizationzForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showOrganizationzInfo", true);
        model.addAttribute("selectedFormOption", "Organización");
        model.addAttribute("organizationzList", organizationzService.getAll());
    }

}