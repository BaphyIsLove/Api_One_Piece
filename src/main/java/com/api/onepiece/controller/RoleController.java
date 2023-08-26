package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Role;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.CharacterEntityRepository;
import com.api.onepiece.repository.RoleRepository;
import com.api.onepiece.service.RoleService;

import jakarta.validation.Valid;

@Controller
public class RoleController {
    
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RoleService roleService;

    @Autowired
    CharacterEntityRepository characterRepository;
    
    @GetMapping("/roleForm")
    public String roleForm(ModelMap model){
        model.addAttribute("roleForm", new Role());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/roleForm")
    public String postRoleForm(@Valid @ModelAttribute("roleForm")Role role, BindingResult result ,ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("roleForm", role);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                roleService.create(role);
                model.addAttribute("roleForm", new Role());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("roleForm", role);
                prepareAttributesFormView(model, "formTab");
            } catch(Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("roleForm", role);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/editRole/{id}")
    public String roleEdit(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            Role roleToEdit = roleService.getById(id);
            model.addAttribute("roleForm", roleToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("/editRole")
    public String postRoleEdit(@Valid @ModelAttribute("roleForm")Role role, BindingResult result, ModelMap model) throws Exception{
        if(result.hasErrors()){
            model.addAttribute("roleForm", role);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                roleService.update(role);
                model.addAttribute("roleForm", new Role());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("editMode", true);
                model.addAttribute("roleForm", role);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteRole/{id}")
    public String deleteRole(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            roleService.delete(id);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "redirect:/roleForm";
    }

    @GetMapping("/roleForm/cancel")
    public String cancelBtn(){
        return "redirect:/roleForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showRoleInfo", true);
        model.addAttribute("selectedFormOption", "Rol");
        model.addAttribute("roleList", roleService.getAll());
        model.addAttribute("character", characterRepository.findAll());
    }

}
