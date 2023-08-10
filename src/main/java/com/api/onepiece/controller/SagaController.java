package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.Saga;
import com.api.onepiece.service.SagaService;

import jakarta.validation.Valid;

@Controller
public class SagaController {

    @Autowired
    SagaService sagaService;
    
    @GetMapping("/sagaForm")
    public String sagaForm(Model model){
        model.addAttribute("sagaForm", new Saga());
        model.addAttribute("sagaList", sagaService.getAllSagas());
        model.addAttribute("selectedFormOption", "saga");
        model.addAttribute("showSagasInfo", true);
        model.addAttribute("listTab", "active");

        return "admin-pages/admin-page";
    }

    @PostMapping("/sagaForm")
    public String createSaga(@Valid @ModelAttribute("sagaForm")Saga saga, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("sagaForm", saga);
            model.addAttribute("formTab", "active");
            model.addAttribute("selectedFormOption", "saga");
            model.addAttribute("showSagasInfo", true);
        } else{
            try {
                sagaService.createSaga(saga);
                model.addAttribute("sagaForm", new Saga());
                model.addAttribute("listTab", "active");
                model.addAttribute("selectedFormOption", "saga");
                model.addAttribute("showSagasInfo", true);
            } catch (DataIntegrityViolationException e){
                model.addAttribute("formErrorMessage", "Una saga con ese nombre ya se encuentra registrada");
                model.addAttribute("sagaForm", saga);
                model.addAttribute("formTab", "active");
                model.addAttribute("selectedFormOption", "saga");
                model.addAttribute("showSagasInfo", true);

            } catch (Exception e) {
                model.addAttribute("formErrorMessage", e);
                model.addAttribute("sagaForm", saga);
                model.addAttribute("formTab", "active");
                model.addAttribute("selectedFormOption", "saga");
                model.addAttribute("showSagasInfo", true);
            }
        }
        model.addAttribute("sagaList", sagaService.getAllSagas());

        return "admin-pages/admin-page";
    }

    @GetMapping("/editSaga/{id}")
    public String editSaga(Model model, @PathVariable(name="id")Long id) throws Exception{ 
        try {
            Saga sagaToEdit = sagaService.getSagaById(id);
            model.addAttribute("sagaForm", sagaToEdit);
            model.addAttribute("sagaList", sagaService.getAllSagas());
            model.addAttribute("formTab", "active");
            model.addAttribute("selectedFormOption", "saga");
            model.addAttribute("showSagasInfo", true);
            model.addAttribute("editMode", true);
        } catch (Exception e) {
            model.addAttribute("formErrorMessage", e);
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("/editSaga")
    public String postEditSaga(@Valid @ModelAttribute("sagaForm") Saga saga, BindingResult result, ModelMap model) {
        try {
            if (result.hasErrors()) {
                model.addAttribute("sagaForm", saga);
                model.addAttribute("formTab", "active");
                model.addAttribute("selectedFormOption", "saga");
                model.addAttribute("showSagasInfo", true);
                model.addAttribute("editMode", true);
            } else {
                sagaService.updateSaga(saga);
            }
        } catch (Exception e) {
            model.addAttribute("formErrorMessage", e.getMessage());
            model.addAttribute("sagaForm", saga);
            model.addAttribute("formTab", "active");
            model.addAttribute("selectedFormOption", "saga");
            model.addAttribute("showSagasInfo", true);
            model.addAttribute("editMode", true);
        }

        return "redirect:/sagaForm";
    }

    @GetMapping("/deleteSaga/{id}")
    public String deleteSaga(Model model, @PathVariable(name = "id")Long id) throws Exception{
        try {
            sagaService.deleteSaga(id);
        } catch (Exception e) {
            model.addAttribute("formErrorMessage", e);
            model.addAttribute("listTab", "active");
            model.addAttribute("selectedFormOption", "saga");
            model.addAttribute("showSagasInfo", true);
        }
        return "redirect:/sagaForm";
    }

    @GetMapping("/sagaForm/cancel")
    public String cancelButton(ModelMap map){
        return "redirect:/sagaForm";
    }



}
