package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Location;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.LocationRepository;
import com.api.onepiece.repository.UbicationRepository;
import com.api.onepiece.service.LocationService;

import jakarta.validation.Valid;

@Controller
public class LocationController {

    @Autowired
    LocationService locationService;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    UbicationRepository ubicationRepository;

    @GetMapping("/locationForm")
    public String locationForm(ModelMap model) {
        model.addAttribute("locationForm", new Location());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/locationForm")
    public String postLocationForm(@Valid @ModelAttribute("locationForm")Location location, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("locationForm", location);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                locationService.create(location);
                model.addAttribute("locationForm", new Location());
                prepareAttributesFormView(model, "listTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("locationForm", location);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("editLocation/{id}")
    public String locationForm(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            Location locationToEdit = locationService.getById(id);
            model.addAttribute("locationForm", locationToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (Exception e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("editLocation")
    public String postLocationEdit(@Valid @ModelAttribute("locationForm")Location location, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("locationForm", location);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                locationService.update(location);
                model.addAttribute("locationForm", new Location());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("locationForm", location);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("locationForm", location);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteLocation/{id}")
    public String deleteLocation(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try{
            locationService.delete(id);
        } catch (MyEntityNotFoundException e){
            throw e;
        }
        return "redirect:/locationForm";
    }
    
    @GetMapping("/locationForm/cancel")
    public String cancelButon(){
        return "redirect:/locationForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showLocationInfo", true);
        model.addAttribute("selectedFormOption", "Raza/Especie");
        model.addAttribute("locationList", locationService.getAll());
        model.addAttribute("ubication", ubicationRepository.findAll());
    }
    
}
