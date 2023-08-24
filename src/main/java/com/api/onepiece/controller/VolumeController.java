package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Volume;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.ArcRepository;
import com.api.onepiece.service.VolumeServiceImpl;

import jakarta.validation.Valid;

@Controller
public class VolumeController {
    
    @Autowired
    VolumeServiceImpl volumeService;
    @Autowired
    ArcRepository arcRepository;

    @GetMapping("/volumeForm")
    public String volumeForm(ModelMap model) throws Exception{
        model.addAttribute("volumeForm", new Volume());
        model.addAttribute("arcs", arcRepository.findAll());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/volumeForm")
    public String createVolume(@Valid @ModelAttribute("volumeForm")Volume volume, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            prepareAttributesFormView(model, "formTab");
            model.addAttribute("arcs", arcRepository.findAll());
            model.addAttribute("volumeForm", volume);
        } else{
            try {
                volumeService.create(volume);
                prepareAttributesFormView(model, "listTab");
                model.addAttribute("volumeForm", new Volume());
                model.addAttribute("arcs", arcRepository.findAll());
            } catch (CustomFieldValidationException e) {
                result.rejectValue(e.getField(), null, e.getMessage());
                prepareAttributesFormView(model, "formTab");
                model.addAttribute("volumeForm", volume);
                model.addAttribute("arcs", arcRepository.findAll());
            } catch(Exception e){
                prepareAttributesFormView(model, "formTab");
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("arcs", arcRepository.findAll());
            }
        }

        return "admin-pages/admin-page";
    }

    @GetMapping("/editVolume/{id}")
    public String editVolume(ModelMap model, @PathVariable(name = "id")Long id) throws Exception{
        try {
            Volume volumeToEdit = volumeService.getById(id);
            prepareAttributesFormView(model, "formTab");
            model.addAttribute("volumeForm", volumeToEdit);
            model.addAttribute("arcs", arcRepository.findAll());
            model.addAttribute("editMode", true);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("/editVolume")
    public String postEditVolume(@Valid @ModelAttribute("volumeForm")Volume volume, BindingResult result, ModelMap model) throws Exception{
        try{
            if(result.hasErrors()){
                model.addAttribute("volumeForm", volume);
                model.addAttribute("editMode", true);
                model.addAttribute("arcs", arcRepository.findAll());
                prepareAttributesFormView(model, "formTab");
            }else{
                volumeService.update(volume);
                prepareAttributesFormView(model, "listTab");
                model.addAttribute("volumeForm", new Volume());
                model.addAttribute("arcs", arcRepository.findAll());
            }
        } catch (CustomFieldValidationException e) {
            result.reject(e.getField(),null, e.getMessage());
            prepareAttributesFormView(model, "formTab");
            model.addAttribute("volumeForm", volume);
            model.addAttribute("arcs", arcRepository.findAll());
            model.addAttribute("editMode", true);
        }

        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteVolume/{id}")
    public String deleteVolume(ModelMap model, @PathVariable(name = "id")Long id) throws Exception{
        try {
            volumeService.delete(id);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "redirect:/volumeForm";
    }

    @GetMapping("/volumeForm/cancel")
    public String cancelButon(){
        return "redirect:/volumeForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showVolumesInfo", true);
        model.addAttribute("selectedFormOption", "Tomo");
        model.addAttribute("volumeList", volumeService.getAll());
    }
}
