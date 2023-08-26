package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Race;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.RaceRepository;
import com.api.onepiece.service.RaceService;

import jakarta.validation.Valid;

@Controller
public class RaceController {
    
    @Autowired
    RaceRepository raceRepository;
    @Autowired
    RaceService raceService;

    @GetMapping("/raceForm")
    public String raceForm(ModelMap model) {
        model.addAttribute("raceForm", new Race());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/raceForm")
    public String postRaceForm(@Valid @ModelAttribute("raceForm")Race race, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("raceForm", race);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                raceService.create(race);
                model.addAttribute("raceForm", new Race());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("raceForm", race);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("raceForm", race);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("editRace/{id}")
    public String raceForm(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            Race raceToEdit = raceService.getById(id);
            model.addAttribute("raceForm", raceToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("editRace")
    public String postRaceEdit(@Valid @ModelAttribute("raceForm")Race race, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("raceForm", race);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                raceService.update(race);
                model.addAttribute("raceForm", new Race());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("raceForm", race);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("raceForm", race);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteRace/{id}")
    public String deleteRace(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try{
            raceService.delete(id);
        } catch (MyEntityNotFoundException e){
            throw e;
        }
        return "redirect:/raceForm";
    }
    
    @GetMapping("/raceForm/cancel")
    public String cancelButon(){
        return "redirect:/raceForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showRaceInfo", true);
        model.addAttribute("selectedFormOption", "Raza/Especie");
        model.addAttribute("raceList", raceService.getAll());
    }

}
