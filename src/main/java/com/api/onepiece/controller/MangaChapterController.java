package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.MangaChapter;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.ArcRepository;
import com.api.onepiece.repository.MangaChapterRepository;
import com.api.onepiece.repository.VolumeRepository;
import com.api.onepiece.service.MangaChapterServiceImpl;

import jakarta.validation.Valid;

@Controller
public class MangaChapterController {
    
    @Autowired
    MangaChapterRepository mangaChapterRepository;

    @Autowired
    MangaChapterServiceImpl mangaChapterService;

    @Autowired
    VolumeRepository volumeRepository;

    @Autowired
    ArcRepository arcRepository;

    @GetMapping("/mangaChapterForm")
    public String mangaChapterForm(ModelMap model){
        model.addAttribute("mangaChapterForm", new MangaChapter());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/mangaChapterForm")
    public String postmangaChapterForm(@Valid @ModelAttribute("mangaChapterForm")MangaChapter mangaChapter, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("mangaChapterForm", mangaChapter);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                mangaChapterService.create(mangaChapter);
                model.addAttribute("mangaChapterForm", new MangaChapter());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                prepareAttributesFormView(model, "formTab");
                model.addAttribute("mangaChapterForm", mangaChapter);
            } catch (Exception e){
                model.addAttribute("mangaChapterForm", mangaChapter);
                model.addAttribute("formErrorMessage", e.getMessage());
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/editMangaChapter/{id}")
    public String editMangaChapter(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            MangaChapter mangaChapterToEdit = mangaChapterService.getById(id);
            model.addAttribute("mangaChapterForm", mangaChapterToEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("/editMangaChapter")
    public String postMangaChapterEdit(@Valid @ModelAttribute("mangaChapterForm")MangaChapter mangaChapter, BindingResult result, ModelMap model) throws Exception{
        if(result.hasErrors()){
            model.addAttribute("mangaChapterForm", mangaChapter);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                mangaChapterService.update(mangaChapter);
                model.addAttribute("mangaChapterForm", new MangaChapter());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("mangaChapterForm", mangaChapter);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("deleteMangaChapter/{id}")
    public String deleteMangaChapter(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            mangaChapterService.delete(id);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "redirect:/mangaChapterForm";
    }
    
    @GetMapping("/mangaChapterForm/cancel")
    public String cancelButon(){
        return "redirect:/mangaChapterForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("volume", volumeRepository.findAll());
        model.addAttribute("mangaArc", arcRepository.findAll());
        model.addAttribute("showMangaChapterInfo", true);
        model.addAttribute("selectedFormOption", "Capitulo del manga");
        model.addAttribute("mangaChapterList", mangaChapterService.getAll());
    }

}
