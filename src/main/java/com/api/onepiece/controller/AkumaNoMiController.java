package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.AkumaNoMi;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.AkumaNoMiRepository;
import com.api.onepiece.service.AkumaNoMiService;

import jakarta.validation.Valid;

@Controller
public class AkumaNoMiController {
    
    @Autowired
    AkumaNoMiRepository akumaNoMiRepository;
    @Autowired
    AkumaNoMiService akumaNoMiService;

    @GetMapping("/akumaNoMiForm")
    public String akumaNoMiForm(ModelMap model){
        model.addAttribute("akumaNoMiForm", new AkumaNoMi());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/akumaNoMiForm")
    public String postAkumaNoMiForm(@Valid @ModelAttribute("akumaNoMiForm")AkumaNoMi akumaNoMi, BindingResult result ,ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("akumaNoMiForm", akumaNoMi);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                akumaNoMiService.create(akumaNoMi);
                model.addAttribute("akumaNoMiForm", new AkumaNoMi());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("akumaNoMiForm", akumaNoMi);
                prepareAttributesFormView(model, "formTab");
            } catch(Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("akumaNoMiForm", akumaNoMi);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/editAkumaNoMi/{id}")
    public String akumaNoMiEdit(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            AkumaNoMi akumaNoMiToEdit = akumaNoMiService.getById(id);
            model.addAttribute("akumaNoMiForm", akumaNoMiToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("/editAkumaNoMi")
    public String postAkumaNoMiEdit(@Valid @ModelAttribute("akumaNoMiForm")AkumaNoMi akumaNoMi, BindingResult result, ModelMap model) throws Exception{
        if(result.hasErrors()){
            model.addAttribute("akumaNoMiForm", akumaNoMi);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                akumaNoMiService.update(akumaNoMi);
                model.addAttribute("akumaNoMiForm", new AkumaNoMi());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("editMode", true);
                model.addAttribute("akumaNoMiForm", akumaNoMi);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteAkumaNoMi/{id}")
    public String deleteAkumaNoMi(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            akumaNoMiService.delete(id);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "redirect:/akumaNoMiForm";
    }

    @GetMapping("/akumaNoMiForm/cancel")
    public String cancelBtn(){
        return "redirect:/akumaNoMiForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showAkumaNoMiInfo", true);
        model.addAttribute("selectedFormOption", "Akuma no mi/Fruta del diablo");
        model.addAttribute("akumaNoMiList", akumaNoMiService.getAll());
    }

}
