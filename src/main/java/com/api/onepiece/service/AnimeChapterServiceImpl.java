package com.api.onepiece.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.onepiece.entity.AnimeChapter;
import com.api.onepiece.error.CustomFieldValidationException;
import com.api.onepiece.error.MyEntityNotFoundException;
import com.api.onepiece.repository.AnimeChapterRepository;
import com.api.onepiece.util.UniqueKeyGenerator;

@Service
public class AnimeChapterServiceImpl implements GenericService<AnimeChapter> {

    @Autowired
    private AnimeChapterRepository animeChapterRepository;

    @Override
    public Iterable<AnimeChapter> getAll() {
        return animeChapterRepository.findAll();
    }

    @Override
    public AnimeChapter getById(Long id) throws Exception {
        return animeChapterRepository.findById(id).orElseThrow(() -> new MyEntityNotFoundException("Id no encontrado"));
    }

    @Override
    public AnimeChapter create(AnimeChapter animeChapter) throws Exception{
        if(animeChapterRepository.existsByName(animeChapter.getName())){
            throw new CustomFieldValidationException("Nombre duplicado", "name");
        } else if(animeChapter.getAnimeArc()==null){
            throw new CustomFieldValidationException("Selecciona un arco", "arc");
        } else {
            Long lastNum = animeChapterRepository.findMaxNumberByPrefix("AC");
            animeChapter.setUniqueKey(UniqueKeyGenerator.generateChapterUniqueKey("AC", lastNum));
            return animeChapterRepository.save(animeChapter);
        }
    }

    @Override
    public AnimeChapter update(AnimeChapter animeChapter) throws Exception {
        AnimeChapter animeChapterToEdit = getById(animeChapter.getId());
        if(!animeChapterToEdit.getName().equals(animeChapterToEdit.getName())&&animeChapterRepository.existsByName(animeChapter.getName())){
            throw new CustomFieldValidationException("Ya existe un capítulo con ese nombre", "name");
        } else if(!animeChapterToEdit.getUniqueKey().equals(animeChapterToEdit.getUniqueKey())&&animeChapterRepository.existsByUniqueKey(animeChapter.getUniqueKey())){
            throw new CustomFieldValidationException("ya existe un capítulo con esa clave", "uniqueKey");
        } else if(animeChapter.getAnimeArc()==null) {
            throw new CustomFieldValidationException("Selecciona un arco", "animeArc");
        } else {
            animeChapterToEdit.setName(animeChapter.getName());
            animeChapterToEdit.setUniqueKey(animeChapter.getUniqueKey());
            animeChapterToEdit.setFillerChapter(animeChapter.isFillerChapter());
            animeChapterToEdit.setAnimeArc(animeChapter.getAnimeArc());
            return animeChapterRepository.save(animeChapterToEdit);
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        animeChapterRepository.deleteById(id);
    }
    
}
