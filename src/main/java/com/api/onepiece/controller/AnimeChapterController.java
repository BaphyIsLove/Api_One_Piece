package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.AnimeChapter;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.ArcRepository;
import com.api.onepiece.service.AnimeChapterServiceImpl;

import jakarta.validation.Valid;

@Controller
public class AnimeChapterController {
    
    @Autowired
    private AnimeChapterServiceImpl animeChapterService;
    @Autowired
    private ArcRepository arcRepository;

    @GetMapping("/animeChapterForm")
    public String animeChapterForm(ModelMap model) {
        model.addAttribute("animeChapterForm", new AnimeChapter());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/animeChapterForm")
    public String postAnimeChapterForm(@Valid @ModelAttribute("animeChapterForm")AnimeChapter animeChapter, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("animeChapterForm", animeChapter);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                animeChapterService.create(animeChapter);
                model.addAttribute("animeChapterForm", new AnimeChapter());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("animeChapterForm", animeChapter);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("animeChapterForm", animeChapter);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("editAnimeChapter/{id}")
    public String AnimeChapterForm(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            AnimeChapter animeChapterToEdit = animeChapterService.getById(id);
            model.addAttribute("animeChapterForm", animeChapterToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("editAnimeChapter")
    public String postAnimeChapterEdit(@Valid @ModelAttribute("animeChapterForm")AnimeChapter animeChapter, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("animeChapterForm", animeChapter);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                animeChapterService.update(animeChapter);
                model.addAttribute("animeChapterForm", new AnimeChapter());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("animeChapterForm", animeChapter);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            } catch (Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("animeChapterForm", animeChapter);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteAnimeChapter/{id}")
    public String deleteAnimeChapter(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try{
            animeChapterService.delete(id);
        } catch (MyEntityNotFoundException e){
            throw e;
        }
        return "redirect:/animeChapterForm";
    }
    
    @GetMapping("/animeChapterForm/cancel")
    public String cancelButon(){
        return "redirect:/animeChapterForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("animeArc", arcRepository.findAll());
        model.addAttribute("showAnimeChapterInfo", true);
        model.addAttribute("selectedFormOption", "Capitulo del anime");
        model.addAttribute("animeChapterList", animeChapterService.getAll());
    }

}
