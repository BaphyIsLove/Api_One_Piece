package com.api.onepiece.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.api.onepiece.entity.CharacterEntity;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.AnimeChapterRepository;
import com.api.onepiece.repository.CharacterEntityRepository;
import com.api.onepiece.repository.CrewRepository;
import com.api.onepiece.repository.MangaChapterRepository;
import com.api.onepiece.repository.OccupationRepository;
import com.api.onepiece.repository.RaceRepository;
import com.api.onepiece.service.CharacterEntityService;

import jakarta.validation.Valid;

@Controller
public class CharacterEntityController {
    
    @Autowired
    CharacterEntityRepository characterRepository;
    @Autowired
    OccupationRepository occupationRepository;
    @Autowired
    CrewRepository crewRepository;
    @Autowired
    RaceRepository raceRepository;
    @Autowired
    MangaChapterRepository mangaRepository;
    @Autowired
    AnimeChapterRepository animeRepository;

    @Autowired
    CharacterEntityService characterService;

    @GetMapping("/characterForm")
    public String characterForm(ModelMap model){
        model.addAttribute("characterForm", new CharacterEntity());
        prepareAttributesFormView(model, "listTab");
        return "admin-pages/admin-page";
    }

    @PostMapping("/characterForm")
    public String postCharacterForm(@Valid @ModelAttribute("characterForm")CharacterEntity character, BindingResult result, ModelMap model) throws Exception{
        if(result.hasErrors()){
            model.addAttribute("characterForm", character);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                characterService.create(character);
                model.addAttribute("characterForm", new CharacterEntity());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("characterForm", character);
                prepareAttributesFormView(model, "formTab");
            } catch(Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("characterForm", character);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/editCharacter/{id}")
    public String editCharacter(@PathVariable(name = "id")Long id, ModelMap model) throws Exception{
        try {
            CharacterEntity toEdit = characterService.getById(id);
            model.addAttribute("characterForm", toEdit);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "admin-pages/admin-page";
    }

    @PostMapping("/editCharacter")
    public String postEditCharacter(@Valid @ModelAttribute("characterForm")CharacterEntity character, BindingResult result, ModelMap model){
        if(result.hasErrors()){
            model.addAttribute("characterForm", character);
            model.addAttribute("editMode", true);
            prepareAttributesFormView(model, "formTab");
        } else{
            try {
                characterService.update(character);
                model.addAttribute("characterForm", new CharacterEntity());
                prepareAttributesFormView(model, "listTab");
            } catch (CustomFieldValidationException e) {
                result.reject(e.getField(), null, e.getMessage());
                model.addAttribute("characterForm", character);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            } catch(Exception e){
                model.addAttribute("formErrorMessage", e.getMessage());
                model.addAttribute("characterForm", character);
                model.addAttribute("editMode", true);
                prepareAttributesFormView(model, "formTab");
            }
        }
        return "admin-pages/admin-page";
    }

    @GetMapping("/deleteCharacter/{id}")
    public String deleteCharacter(@PathVariable(name = "id")Long id) throws Exception{
        try {
            characterService.delete(id);
        } catch (MyEntityNotFoundException e) {
            throw e;
        }
        return "redirect:/characterForm";
    }

    @GetMapping("/characterForm/cancel")
    public String cancelBtn(){
        return "redirect:/characterForm";
    }

    private void prepareAttributesFormView(ModelMap model, String listTabOrFormTab){
        model.addAttribute(listTabOrFormTab, "active");
        model.addAttribute("showCharacterInfo", true);
        model.addAttribute("selectedFormOption", "Personaje");
        model.addAttribute("characterList", characterService.getAll());
        model.addAttribute("occupation", occupationRepository.findAll());
        model.addAttribute("crew", crewRepository.findAll());
        model.addAttribute("race", raceRepository.findAll());
        model.addAttribute("firstApparitionAnime", animeRepository.findAll());
        model.addAttribute("firstApparitionManga", mangaRepository.findAll());

    }
}
